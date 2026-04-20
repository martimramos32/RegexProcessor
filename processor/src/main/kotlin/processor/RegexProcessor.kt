package processor
import annotations.Extract
import com.google.auto.service.AutoService
import com.squareup.kotlinpoet.*
import java.io.File
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic

/**
 * Processador de anotações responsável por processar a anotação [Extract].
 * Adicionalmente a esta tarefa, gera uma classe wrapper para qualquer classe que contenha métodos abstratos
 * que estejam anotados com [Extract]. A classe gerada irá implementar esses mesmos métodos para conseguir
 * executar a expressão regular fornecida sobre a string de entrada, retornando assim o valor correspondente (o primeiro grupo).
 */
@AutoService(Processor::class)
@SupportedSourceVersion(SourceVersion.RELEASE_25)
@SupportedAnnotationTypes("annotations.Extract")
class RegexProcessor : AbstractProcessor() {

    /**
     * Processa as anotações geradas.
     *
     * @param annotations Os tipos de anotação solicitados para serem processados.
     * @param roundEnv O ambiente com informações sobre a ronda de processamento atual e a anterior.
     * @return Retorna true caso as anotações sejam reclamadas por este processador, false caso contrário.
     */
    override fun process(annotations: MutableSet<out TypeElement>,
                         roundEnv: RoundEnvironment): Boolean {
        val classMethodMap = mutableMapOf<TypeElement,
                MutableList<ExecutableElement >>()
        // Find all methods annotated with @Extract
        for (element in
        roundEnv.getElementsAnnotatedWith(Extract::class.java)) {
            if (element is ExecutableElement) {
                val enclosingClass = element.enclosingElement as
                        TypeElement
                classMethodMap.computeIfAbsent(enclosingClass) {
                    mutableListOf() }.add(element)
            }
        }
        // Generate wrapper classes for each class containing annotated methods
        for ((classElement, methods) in classMethodMap) {
            generateKotlinWrapperClass(classElement, methods)
        }
        return true
    }

    /**
     * Gera uma classe de envolvimento (wrapper class) em Kotlin que implementa os métodos anotados.
     *
     * @param classElement O elemento de tipo sintático que representa a classe que contém os métodos anotados.
     * @param methods A lista de elementos executáveis correspondentes aos métodos anotados com [Extract].
     */
    private fun generateKotlinWrapperClass(classElement: TypeElement,
                                           methods: List<ExecutableElement >) {
        val packageName =
            processingEnv.elementUtils.getPackageOf(classElement).toString()
        val originalClassName = classElement.simpleName.toString()
        val extractorClassName = "${originalClassName}Extractor"
        // Create the wrapper class using composition
        val classBuilder = TypeSpec.classBuilder(extractorClassName)
            .primaryConstructor(
                FunSpec.constructorBuilder()
                    .addParameter("input", String::class) //Recebe apenas a String "input"
                    .build()
            )
            .superclass(ClassName(packageName,originalClassName)) //Herda de DataProcessor
            .addSuperclassConstructorParameter("input") // Passa o 'input' para o construtor pai
            .addModifiers(KModifier.PUBLIC)

        // Generate wrapper methods
        for (method in methods) {
            val methodName = method.simpleName.toString()

            // Vai buscar o valor da anotação
            val extractAnnotation = method.getAnnotation(Extract::class.java)
            val regexString = extractAnnotation?.regex ?: ""

            // Construir o novo metodo
            val methodBuilder = FunSpec.builder(methodName)
                .addModifiers(KModifier.OVERRIDE) // Tem de ser override para que o codigo saiba que tem de subscrever essas determinadas funções chamadas
                .returns(String::class.asTypeName().copy(nullable = true)) // Devolve String?
                .addStatement("val match = %T(%S).find(input)", Regex::class, regexString)
                .addStatement("return match?.groupValues?.get(1)")

            classBuilder.addFunction(methodBuilder.build())
        }
        // Build the Kotlin file
        val file = FileSpec.builder(packageName, extractorClassName)
            .addType(classBuilder.build())
            .build()
        // Write the generated file
        try {
            val kaptKotlinGeneratedDir =
                processingEnv.options["kapt.kotlin.generated"]
            if (kaptKotlinGeneratedDir != null) {
                file.writeTo(File(kaptKotlinGeneratedDir)) // Correct way to write Kotlin files
            } else {
                processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                    "kapt.kotlin.generated not found")
            }
        } catch (e: Exception) {
            processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                "Error generating Kotlin file: ${e.message}")
        }
    }
}