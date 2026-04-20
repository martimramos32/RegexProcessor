package app
import annotations.Extract

/**
 * Classe abstrata responsável por demonstrar a utilização do processador de expressões regulares (Regex Processor).
 * Define métodos abstratos anotados com [Extract] para extrair informação específica
 * da string [input] fornecida, utilizando expressões regulares para o efeito.
 *
 * @property input A string de origem a partir da qual os dados serão extraídos.
 */
abstract class DataProcessor(val input: String) {
    /**
     * Extrai o nome a partir da string de entrada.
     *
     * @return O nome extraído, ou null caso não seja encontrada nenhuma correspondência.
     */
    @Extract(regex = "Name: (\\w+)")
    abstract fun getName(): String?

    /**
     * Extrai a morada a partir da string de entrada.
     *
     * @return A morada extraída, ou null caso não seja encontrada nenhuma correspondência.
     */
    @Extract(regex = "Address: (.+)")
    abstract fun getAddress(): String?
}