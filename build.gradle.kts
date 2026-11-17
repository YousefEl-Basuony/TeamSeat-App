plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    // فك الكومنت لما تضيف ملف google-services.json في فولدر app:
    // id("com.google.gms.google-services") version "4.4.2" apply false
}
