package org.redesnac.lsgbible.data.remote.dto.verse

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import org.redesnac.lsgbible.domain.model.verse.Verse

data class Result (
    val chapterVerseRaw: String,
    val nav: Nav,
    val bookRaw: String,
    val versesCount: Long,
    val chapterVerse: String,
    val verseIndex: VerseIndex,
    val bookID: Long,
    val bookName: String,
    val bookShort: String,
    val verses: Verses,
    val singleVerse: Boolean
)

data class Nav (

    @SerializedName("prev_book"    ) var prevBook    : String? = null,
    @SerializedName("next_book"    ) var nextBook    : String? = null,
    @SerializedName("next_chapter" ) var nextChapter : String? = null,
    @SerializedName("ncb_name"     ) var ncbName     : String? = null,
    @SerializedName("prev_chapter" ) var prevChapter : String? = null,
    @SerializedName("pcb_name"     ) var pcbName     : String? = null,
    @SerializedName("cur_chapter"  ) var curChapter  : String? = null,
    @SerializedName("ccb_name"     ) var ccbName     : String? = null,
    @SerializedName("ncb"          ) var ncb         : Int?    = null,
    @SerializedName("ncc"          ) var ncc         : Int?    = null,
    @SerializedName("pcc"          ) var pcc         : Int?    = null,
    @SerializedName("pcb"          ) var pcb         : Int?    = null,
    @SerializedName("ccb"          ) var ccb         : String? = null,
    @SerializedName("ccc"          ) var ccc         : String? = null,
    @SerializedName("nb"           ) var nb          : Int?    = null,
    @SerializedName("pb"           ) var pb          : Int?    = null

)

data class VerseIndex (
    val the1: List<String>
)

data class Verses (
    val segond_1910: Segond_1910
)

data class Segond_1910 (
    @SerializedName(value="1", alternate = [
        "2",
        "3",
        "4",
        "5",
        "6",
        "7",
        "8",
        "9",
        "10",
        "11",
        "12",
        "13",
        "14",
        "15",
        "16",
        "17",
        "18",
        "19",
        "20",
        "21",
        "22",
        "23",
        "24",
        "25",
        "26",
        "27",
        "28",
        "29",
        "30",
        "31",
        "32",
        "33",
        "34",
        "35",
        "36",
        "37",
        "38",
        "39",
        "40",
        "41",
        "42",
        "43",
        "44",
        "45",
        "46",
        "47",
        "48",
        "49",
        "50",
        "51",
        "52",
        "53",
        "54",
        "55",
        "56",
        "57",
        "58",
        "59",
        "60",
        "61",
        "62",
        "63",
        "64",
        "65",
        "66",
        "67",
        "68",
        "69",
        "70",
        "71",
        "72",
        "73",
        "74",
        "75",
        "76",
        "77",
        "78",
        "79",
        "80",
        "81",
        "82",
        "83",
        "84",
        "85",
        "86",
        "87",
        "88",
        "89",
        "90",
        "91",
        "92",
        "93",
        "94",
        "95",
        "96",
        "97",
        "98",
        "99",
        "100",
        "101",
        "102",
        "103",
        "104",
        "105",
        "106",
        "107",
        "108",
        "109",
        "110",
        "111",
        "112",
        "113",
        "114",
        "115",
        "116",
        "117",
        "118",
        "119",
        "120",
        "121",
        "122",
        "123",
        "124",
        "125",
        "126",
        "127",
        "128",
        "129",
        "130",
        "131",
        "132",
        "133",
        "134",
        "135",
        "136",
        "137",
        "138",
        "139",
        "140",
        "141",
        "142",
        "143",
        "144",
        "145",
        "146",
        "147",
        "148",
        "149",
        "150"
    ])
    @Expose
    val the1: Map<String, VerseDto>
)

data class VerseDto (
    val chapter: String,
    val book: String,
    val id: String,
    val verse: String,
    val text: String
) {
    fun toVerse():Verse {
        return Verse(
            chapter = chapter,
            verse = verse,
            text = text
        )
    }
}
