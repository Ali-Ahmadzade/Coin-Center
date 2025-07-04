package test.example.coincenter.apimanagers.models


import com.google.gson.annotations.SerializedName

data class NewsData(
    @SerializedName("next")
    val next: Any,
    @SerializedName("previous")
    val previous: Any,
    @SerializedName("results")
    val results: List<Result>
) {
    data class Result(
        @SerializedName("created_at")
        val createdAt: String,
        @SerializedName("description")
        val description: String,
        @SerializedName("id")
        val id: Int,
        @SerializedName("kind")
        val kind: String,
        @SerializedName("published_at")
        val publishedAt: String,
        @SerializedName("slug")
        val slug: String,
        @SerializedName("title")
        val title: String
    )
}