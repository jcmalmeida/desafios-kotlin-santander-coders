private const val PAES = 1
private const val SALGADOS = 2
private const val DOCES = 3
private const val FINALIZAR = 0

private const val LINHA = ".........."

private const val CUPOM_5PADOCA = 0.05
private const val CUPOM_10PADOCA = 0.10
private const val CUPOM_5OFF = 5

val valorPaoFrances = 0.60
val valorPaoDeLeite = 0.40
val valorPaoDeMilho = 0.50

val valorCoxinha = 5.00
val valorEsfiha = 6.00
val valorPaoDeQueijo = 3.00

val valorCarolina = 1.50
val valorPudim = 4.00
val valorBrigadeiro = 2.00

val produtoPaoFrances = "Pão Francês"
val produtoPaoDeLeite = "Pão de Leite"
val produtoPaoDeMilho = "Pão de Milho"

val produtoCoxinha = "Coxinha"
val produtoEsfiha = "Esfiha"
val produtoPaoDeQueijo = "Pão de Queijo"

val produtoCarolina = "Carolina"
val produtoPudim = "Pudim"
val produtoBrigadeiro = "Brigadeiro"

val paes: List<Pair<String, Double>> = listOf(
    Pair(produtoPaoFrances, valorPaoFrances), // index 0
    Pair(produtoPaoDeLeite, valorPaoDeLeite), // index 1
    Pair(produtoPaoDeMilho, valorPaoDeMilho), // index 2
)

val salgados: List<Pair<String, Double>> = listOf(
    Pair(produtoCoxinha, valorCoxinha),         // index 0
    Pair(produtoEsfiha, valorEsfiha),           // index 1
    Pair(produtoPaoDeQueijo, valorPaoDeQueijo), // index 2
)

val doces: List<Pair<String, Double>> = listOf(
    Pair(produtoCarolina, valorCarolina),
    Pair(produtoPudim, valorPudim),
    Pair(produtoBrigadeiro, valorBrigadeiro)
)

val categorias = """
    Digite a opção desejada no menu:
    1 .................. Pães
    2 .............. Salgados
    3 ................. Doces
    0 ...... Finalizar compra
""".trimIndent()

val menuPaes = """
        1. ${produtoPaoFrances.padEnd(20 , '.')} R$ ${String.format("%.2f", valorPaoFrances)}
        2. ${produtoPaoDeLeite.padEnd(20 , '.')} R$ ${String.format("%.2f", valorPaoDeLeite)}
        3. ${produtoPaoDeMilho.padEnd(20 , '.')} R$ ${String.format("%.2f", valorPaoDeMilho)}
        0. Voltar
    """.trimIndent()

val menuSalgados = """
        1. ${produtoCoxinha.padEnd(20 , '.')} R$ ${String.format("%.2f", valorCoxinha)}
        2. ${produtoEsfiha.padEnd(20 , '.')} R$ ${String.format("%.2f", valorEsfiha)}
        3. ${produtoPaoDeQueijo.padEnd(20 , '.')} R$ ${String.format("%.2f", valorPaoDeQueijo)}
        0. Voltar
    """.trimIndent()

val menuDoces = """
        1. ${produtoCarolina.padEnd(20 , '.')} R$ ${String.format("%.2f", valorCarolina)}
        2. ${produtoPudim.padEnd(20 , '.')} R$ ${String.format("%.2f", valorPudim)}
        3. ${produtoBrigadeiro.padEnd(20 , '.')} R$ ${String.format("%.2f", valorBrigadeiro)}
        0. Voltar
    """.trimIndent()

val itensComanda: MutableList<String> = mutableListOf()
var total: Double = 0.0

fun main() {
    do {
        var finalizarCompra = "S"
        ePadoca()

        if (itensComanda.isEmpty()) {
            println("Deseja mesmo finalizar a compra? (S/N)")
            finalizarCompra = readln().uppercase()
        } else {
            encerraComanda()
        }
    } while (finalizarCompra != "S")
}

fun ePadoca() {
    println("Bem Vindo à Padaria E-Padoca!")
    do {
        println(categorias)
        val categoria = readln().toInt()
        when (categoria) {
            PAES -> selecionaProduto(menuSelecionado = menuPaes, produtos = paes)
            SALGADOS -> selecionaProduto(menuSelecionado = menuSalgados, produtos = salgados)
            DOCES -> selecionaProduto(menuSelecionado = menuDoces, produtos = doces)
            else -> Unit
        }
    } while (categoria != FINALIZAR)
}

fun selecionaProduto(
    menuSelecionado: String,
    produtos: List<Pair<String, Double>>
) {
    do {
        println(menuSelecionado)
        val produtoSelecionado = readln().toInt() // valor atual -> 1 (corresponde ao pao frances)

        for ((i, produto) in produtos.withIndex()) {
            if (i.inc() == produtoSelecionado) {
                selecionaQuantidadeDoProduto(produto)
                break
            }
        }
    } while (produtoSelecionado != 0)
}

fun selecionaQuantidadeDoProduto(produto: Pair<String, Double>) {
    println("Digite a quantidade:")
    val quantidade = readln().toInt()
    val totalItem = quantidade * produto.second
    val item =
        itemComanda(produto = produto.first, quantidade = quantidade, valorUnitario = produto.second, total = totalItem)
    itensComanda.add(item)
    total += totalItem
}

fun itemComanda(
    produto: String,
    quantidade: Int,
    valorUnitario: Double,
    total: Double,
): String {
    val produtoNaComanda = produto.padEnd(18, '.')
    val itemComanda = "${itensComanda.size.inc()} $LINHA $produtoNaComanda $quantidade $LINHA R$ ${String.format("%.2f", valorUnitario)} ${LINHA} R$ ${String.format("%.2f", total)}"
    return itemComanda
}

fun encerraComanda() {
    do {
        var cupomValido = false
        var usarCupom = ""

        do {
            println("Deseja aplicar um cupom de desconto? (S/N)")
            usarCupom = readln().uppercase()
        } while (usarCupom != "S" && usarCupom != "N")

        if (usarCupom == "S") {
            println("Digite o cupom que deseja utilizar:")
            val cupom = readln().uppercase()
            cupomValido = (cupom == "5PADOCA") || (cupom == "10PADOCA") || (cupom == "5OFF")
            total = calculaValorTotal(total, cupom)
            if (!cupomValido) println("Cupom inválido.")
        } else if (usarCupom == "N") {
            cupomValido = true
        }

    } while (!cupomValido)

    println()
    println("========================== Comanda E-Padoca =============================")
    println("Item ....... Produto .......... Qtd ......... Valor ............ Total")
    itensComanda.forEach { item -> // funcao
        println(item)
    }
    println("================================================================ Total")
    println("==============================================================> R$ ${String.format("%.2f", total)}")
    println("========================== VOLTE SEMPRE ^-^ ===========================")
}

fun calculaValorTotal(total: Double, cupom: String): Double {
    val valorFinal = when (cupom) {
        "5PADOCA" -> total * (1 - CUPOM_5PADOCA)
        "10PADOCA" -> total * (1 - CUPOM_10PADOCA)
        "5OFF" -> total - CUPOM_5OFF
        else -> total
    }
    return valorFinal
}