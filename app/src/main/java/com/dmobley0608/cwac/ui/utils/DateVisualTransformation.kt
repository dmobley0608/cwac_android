package com.dmobley0608.cwac.ui.utils


import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
class DateVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        // Make the string DD-MM-YYYY
        val trimmed = if (text.text.length >= 8) text.text.substring(0..7) else text.text
        var output = ""
        for (i in trimmed.indices) {
            output += trimmed[i]
            if (i < 4 && i % 2 == 1) output += "-"
        }
        val dateTranslator = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                // [offset [0 - 1] remain the same]
                if (offset <= 1) return offset
                // [2 - 3] transformed to [3 - 4] respectively
                if (offset <= 3) return offset + 1
                // [4 - 7] transformed to [6 - 9] respectively
                if (offset <= 7) return offset + 2
                return 10
            }

            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 1) return offset
                if (offset <= 4) return offset - 1
                if (offset <= 9) return offset - 2
                return 8
            }

        }

        return TransformedText(
            AnnotatedString(output),
            dateTranslator
        )
    }
}