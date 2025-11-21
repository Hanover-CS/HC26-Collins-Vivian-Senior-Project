package org.example.pagepalapp.ui.quotes

val motivationalQuotes = listOf(
    "Keep turning the page — your story isn’t finished yet.",
    "One chapter at a time. One day at a time.",
    "You’re doing amazing. Keep going.",
    "Small steps still move you forward.",
    "Believe in the magic of new beginnings.",
    "Books aren’t the only thing worth reading — you are, too.",
    "Progress > perfection.",
    "You are building something beautiful. Don’t stop."
)

fun randomQuote(): String {
    return motivationalQuotes.random()
}
