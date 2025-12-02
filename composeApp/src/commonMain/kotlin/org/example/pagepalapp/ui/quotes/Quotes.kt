/**
 * a list of quotes that are randomly displayed in the "QuoteCard" on the home screen.
 */

package org.example.pagepalapp.ui.quotes

val motivationalQuotes = listOf(
    "Keep turning the page — your story isn’t finished yet.",
    "One chapter at a time. One day at a time.",
    "You’re doing amazing. Keep going.",
    "Small steps still move you forward.",
    "Believe in the magic of new beginnings.",
    "Books aren’t the only thing worth reading — you are, too.",
    "Progress > perfection.",
    "Every page you turn is a step toward a wiser, braver version of yourself.",
    "\"A reader lives a thousand lives before he dies . . . The man who never reads lives " +
            "only one.\" - George R.R. Martin",
    "\"The more that you read, the more things you will know. The more that you learn, the " +
            " more places you’ll go.\" - Dr. Seuss",
    "\"Books are a uniquely portable magic.\" - Stephen King",
    "\"No. I can survive well enough on my own—if given the proper reading material.\" - Sarah J. Maas",
    "\"These books gave Matilda a hopeful and comforting message: You are not alone.\" - Roald Dahl",
    "\"Don’t feel bad for one moment about doing what brings you joy.\" - Rhysand (A Court of Thorns " +
            "and Roses)"
)

fun randomQuote(): String {
    return motivationalQuotes.random()
}
