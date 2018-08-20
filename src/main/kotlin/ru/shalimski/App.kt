
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML
import java.io.File
import kotlin.math.round

fun main(args: Array<String>) {

    if (args.size != 1) println("I work with one argument. It may be XML file from mts.ru .")

    val file = File(args[0])

    if (!file.isFile) return

    val xmlString = file.readText()

    val jsonObject = XML.toJSONObject(xmlString)

    val arr = jsonObject.query("/Report/ds/i")

    val map = mutableMapOf<String, Float>()

    (arr as JSONArray).forEach {
        val datetime = if ((it as JSONObject).has("bd")) it.get("bd") as String else it.get("d") as String
        val my = datetime.slice(3..9)

        val kb = it.get("du")

        if (kb is String && kb.endsWith("Kb")) {

            val ff = round(kb.dropLast(2).toFloat())


            if (map.containsKey(my))
                map[my] = map[my]!!.plus(ff)
            else
                map[my] = ff
        }
    }

    map.forEach { println(it.key + " " + "%.2f".format(it.value / 1048576) + " GB") }

}