private const val ADD_ITEM = 1
private const val EDIT_ITEM = 2
private const val SHOW_ALL_ITEMS_IN_STOCK = 3
private const val SHOW_ALL_ITEMS = 4
private const val CLOSE_SYSTEM = 0

private const val MAX_CHARACTERS_ITEM_NAME = 20
private const val MAX_ITEM_QTY = 999

var itemIDNumber = 1
var stock: MutableList<Triple<Int, String, Int>> = mutableListOf()

fun main() {
    println()
    println("Sistema de estoque da Oficina Mecânica do Bairro:")
    stockControl()
}

fun stockControl() {
    val mainMenu = """
    
        1 ...... ADICIONAR ITEM
        2 ...... EDITAR ITEM
        3 ...... EXIBIR ITENS EM ESTOQUE
        4 ...... EXIBIR TODOS OS ITENS
        0 ...... FECHAR SISTEMA
        """.trimIndent()

    do {
        println(mainMenu)

        val task = readln().toIntOrNull() ?: -1
        when (task) {
            ADD_ITEM -> addItem()
            EDIT_ITEM -> editItem()
            SHOW_ALL_ITEMS_IN_STOCK -> showAllItems(onlyInStock = true)
            SHOW_ALL_ITEMS -> showAllItems(onlyInStock = false)
            CLOSE_SYSTEM -> closeSystem()
            else -> println("Opção inválida!")
        }
    } while (task != CLOSE_SYSTEM)
}

fun addItem() {
    val addItemHeader = """
        
    ---------------------------------------
    Adição de item ao estoque - ID #$itemIDNumber
    ---------------------------------------
 
    """.trimIndent()
    println(addItemHeader)

    println("Digite o nome da peça (máx. $MAX_CHARACTERS_ITEM_NAME caracteres):")
    val itemName = readAndValidateItemName()

    println("Digite a quantidade em estoque da peça (máx. $MAX_ITEM_QTY):")
    val qtyInStock = readAndValidateItemQty()

    stock.add(Triple(itemIDNumber, itemName, qtyInStock))
    itemIDNumber++
}

fun editItem() {
    if (itemIDNumber == 1) {
        val editItemText = """
            
            Não há itens cadastrados no estoque.
        """.trimIndent()
        println(editItemText)

    } else {
        val itemEditionHeader = """
        
        ---------------------------------------
        Edição de item do estoque
        ---------------------------------------
    
        """.trimIndent()
        println(itemEditionHeader)

        var itemNewName: String = ""
        println("Digite o ID da peça que deseja editar:")
        val itemID = readAndValidateItemID()

        println("Deseja modificar o nome da peça? (S/N)")
        do {
            val modifyItemName = readln().uppercase()

            if (modifyItemName == "S") {
                println("Insira o novo nome da peça (máx. $MAX_CHARACTERS_ITEM_NAME caracteres):")
                itemNewName = readAndValidateItemName()
            } else if (modifyItemName == "N") {
                itemNewName = stock[itemID - 1].second
            } else {
                println("Opção inválida. Digite novamente:")
            }
        } while (modifyItemName != "S" && modifyItemName != "N")

        println("Digite a nova quantidade em estoque da peça (máx. $MAX_ITEM_QTY):")
        val qtyInStock = readAndValidateItemQty()

        stock[itemID - 1] = Triple(itemID, itemNewName, qtyInStock)
    }
}

fun showAllItems(onlyInStock: Boolean) {
    val stockHeader = """
        
    ***************************************
    ID | Peça                 | Quantidade
    ***************************************
    """.trimIndent()
    println(stockHeader)

    for (item in stock) {
        if ((onlyInStock && item.third > 0) || (!onlyInStock)) {
            println("#${item.first} | ${item.second} | ${item.third}")
        }
    }
}

fun closeSystem() {
    val closeSystemText = """
        
        Encerrando o sistema de estoque...
        ---------------------------------------
        """.trimIndent()
    println(closeSystemText)
}

fun readAndValidateItemID(): Int {
    var itemID: Int?
    var validItemID = false

    do {
        itemID = readln().toIntOrNull()
        if (itemID != null) {
            validItemID = (itemID > 0) && (itemID < itemIDNumber)
        }
        if (!validItemID) {
            println("ID inválido. Por favor, digite novamente:")
        }
    } while (!validItemID)

    return itemID!!
}

fun readAndValidateItemName(): String {
    var itemName: String = ""

    do {
        itemName = readln()
        if (itemName.length > MAX_CHARACTERS_ITEM_NAME) {
            println("Número máximo de caracteres ($MAX_CHARACTERS_ITEM_NAME) excedido, digite novamente:")
        } else if (itemName.isEmpty()) {
            println("Por favor, insira o nome da peça:")
        }
    } while (itemName.length > MAX_CHARACTERS_ITEM_NAME || itemName.isEmpty())

    for (i in itemName.length until MAX_CHARACTERS_ITEM_NAME) {
        itemName = "$itemName "
    }

    return itemName
}

fun readAndValidateItemQty(): Int {
    var qtyInStock: Int?

    do {
        qtyInStock = readln().toIntOrNull()

        try {
            when (qtyInStock) {
                in Int.MIN_VALUE until 0 -> throw LimiteEstoqueMinException()
                in MAX_ITEM_QTY + 1..Int.MAX_VALUE -> throw LimiteEstoqueMaxException()
                null -> throw NullPointerException()
            }
        } catch (e: NullPointerException) {
            println("A quantidade digitada é inválida. Por favor, digite novamente:")
        } catch (e: LimiteEstoqueMinException) {
            println("A quantidade digitada é inválida. Por favor, digite novamente:")
        } catch (e: LimiteEstoqueMaxException) {
            println("A quantidade máxima de determinado item em estoque é de $MAX_ITEM_QTY. Por favor, digite novamente:")
        }
    } while (qtyInStock == null || qtyInStock < 0 || qtyInStock > MAX_ITEM_QTY )

    return qtyInStock!!
}

class LimiteEstoqueMinException: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: a quantidade mínima de um determinado item em estoque não pode ser menor do que 0."
    }
}

class LimiteEstoqueMaxException: Exception() {
    override fun getLocalizedMessage(): String {
        return "Erro: A quantidade máxima de um determinado item em estoque não pode ser maior do que $MAX_ITEM_QTY."
    }
}