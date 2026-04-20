package app

/**
 * Ponto de entrada principal da aplicação.
 * Aqui é demonstrado como inicializar e utilizar a classe autogerada previamente `DataProcessorExtractor`
 * com a finalidade de extrair dados de uma string de exemplo consoante as expressões regulares parametrizadas.
 */
fun main() {
    val input = "Name: John Address: 123 Street"
    // Using the generated DataProcessorExtractor
    val extractor = DataProcessorExtractor(input)
    println("Name: ${extractor.getName()}")
    println("Address: ${extractor.getAddress()}")
}