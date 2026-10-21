package com.example.paintcover.data.model

sealed interface PaintCoverError {
    val message: String

    data object EmptyInput : PaintCoverError {
        override val message: String = "Please fill in all required fields."
    }

    data object InvalidNumber : PaintCoverError {
        override val message: String = "Enter a valid number."
    }

    data object NonPositiveArea : PaintCoverError {
        override val message: String = "Surface area must be greater than zero."
    }

    data object NonPositiveDimension : PaintCoverError {
        override val message: String = "Dimensions must be greater than zero."
    }

    data object NonPositiveCoverage : PaintCoverError {
        override val message: String = "Paint coverage must be greater than zero."
    }

    data object InvalidCoats : PaintCoverError {
        override val message: String = "Number of coats must be a positive whole number."
    }

    data object InvalidWastePercent : PaintCoverError {
        override val message: String = "Waste allowance must be between 0% and 100%."
    }

    data object NonPositiveCanSize : PaintCoverError {
        override val message: String = "Can size must be greater than zero."
    }

    data object OpeningTooLarge : PaintCoverError {
        override val message: String = "Opening area must be smaller than the surface area."
    }

    data object InputTooLong : PaintCoverError {
        override val message: String = "Number is too long. Maximum 500 digits."
    }
}
