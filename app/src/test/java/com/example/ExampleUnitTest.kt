package com.example

import com.example.ui.localization.AppLanguage
import com.example.ui.localization.GramSevaStrings
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun addition_isCorrect() {
    assertEquals(4, 2 + 2)
  }

  @Test
  fun testLocalizationStrings() {
    assertEquals("ग्रामसेवा", GramSevaStrings.appTitle(AppLanguage.MARATHI))
    assertEquals("GramSeva", GramSevaStrings.appTitle(AppLanguage.ENGLISH))
    assertEquals("नवीन तक्रार नोंदणी", GramSevaStrings.registerNewComplaint(AppLanguage.MARATHI))
  }
}
