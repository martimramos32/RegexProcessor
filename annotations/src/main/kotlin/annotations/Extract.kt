package annotations

/**
 * Anotação utilizada para especificar uma expressão regular com o objetivo de extrair dados de uma string.
 * Esta anotação deve ser aplicada a funções abstratas.
 * O processador de anotações irá gerar uma implementação para estas funções
 * que utiliza a [regex] fornecida para extrair um valor da string de entrada.
 *
 * @property regex O padrão de expressão regular a ser utilizado para a extração.
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.SOURCE)
annotation class Extract(val regex: String)