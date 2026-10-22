# 💺 StackMates (RoleSpot) — نظام حجز مقاعد تيمات المشاريع

> **An Android application that solves the chaos of university team formation using an interactive Cinema/Bus-style Seat Reservation System.**

![Android](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)
![Room DB](https://img.shields.io/badge/Room%20Database-Local-FF6F00?style=for-the-badge&logo=sqlite&logoColor=white)

---

## 📌 فكرة المشروع (Overview)
يقضي التطبيق على عشوائية تكوين التيمات على جروبات الدفعة (سواء لمشاريع التخرج، السكاشن، أو المسابقات). بدلاً من البحث العشوائي، يقوم **قائد التيم (Team Leader)** بإنشاء مشروع وتحديد المقاعد الفارغة والتخصص المطلوب لكل مقعد (مثل: `1 Android`, `2 UI/UX`, `1 Backend`)، ويدخل الطلبة لاختيار وحجز المقعد المناسب لمهاراتهم تماماً مثل حجز مقاعد السينما أو الباص.

### 🎯 المشكلة التي يحلها التطبيق:
1. **للطلبة:** وضوح التيمات المتاحة، العدد الناقص في كل تيم، والتخصصات المطلوبة بالضبط.
2. **لقائد التيم:** تنظيم طلبات الانضمام وقبول الشخص المناسب لكل تخصص بضغطة زر.
3. **للمعيدين (Teaching Assistants):** استلام قوائم التيمات مكتملة ومنظمة بالأدوار جاهزة بدون تكرار أو نقص.

---

## ✨ المميزات الأساسية (Key Features)

* **💺 خريطة مقاعد تفاعلية (Interactive Seat Map):** عرض مقاعد التيم بـ 3 حالات واضحة:
  * 🟢 **متاح (Available):** مقعد فارغ ينتظر طالباً بالتخصص المطلوب.
  * 🟡 **قيد المراجعة (Pending):** طالب قامبطلب حجز المقعد وفي انتظار موافقة القائد.
  * 🔵 **محجوز (Booked):** تم اعتماد الطالب وتثبيت المقعد باسمه رسمياً.
* **🔄 وضع العرض التفاعلي للمناقشة (Live Demo Switcher Mode):**
  * شريط علوي ذكي يسمح بالتبديل اللحظي بضغطة زر واحدة بين حساب **(أحمد - قائد التيم)** وحساب **(سارة - طالبة)** على نفس الجهاز لاستعراض دورة الحجز والقبول بالكامل أمام المعيد بدون الحاجة لسيرفر خارجي.
* **📋 تصدير كشف التيم للمعيد (TA Sheet Export):** إمكانية عرض وتصدير جدول التيم النهائي شاملاً أسماء الطلبة وتخصصاتهم.
* **⚡ العمل بالكامل بدون إنترنت (100% Offline-First):** يعتمد على قاعدة بيانات محلية سريعة مع تحديث لحظي للواجهات.

---

## 🛠️ التقنيات المستخدمة (Tech Stack & Architecture)

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose (Material Design 3)
* **Architecture:** MVVM (Model - View - ViewModel)
* **Local Database:** Room Database (Single Source of Truth shared across Demo personas)
* **Reactive State:** Kotlin Coroutines & `StateFlow` / `Flow`
* **Navigation:** Compose Navigation / Voyager

---

## 🎬 سيناريو التجربة السريعة (Demo Scenario)

1. **بحساب أحمد (Leader):** إنشاء مشروع جديد وفتح **6 مقاعد** بتخصصات محددة.
2. **التبديل بضغطة زر لحساب سارة (Student):** استعراض المقاعد الفارغة، اختيار مقعد `Android Developer`، والضغط على **"احجز المقعد"** (تتحول حالة المقعد إلى `Pending ⏳`).
3. **العودة لحساب أحمد (Leader):** ظهور إشعار لحظي بطلب سارة، وعند الضغط على **"قبول ✅"** يتحول المقعد إلى `Booked` ويُغلق رسمياً باسم سارة.

---

## 🗄️ هيكلة قاعدة البيانات المبدئية (Database Schema)

* `TeamsTable`: (`teamId`, `projectName`, `category`, `leaderId`, `description`)
* `SeatsTable`: (`seatId`, `teamId`, `roleName`, `status [AVAILABLE | PENDING | BOOKED]`, `applicantId`, `applicantName`)

---

## 🚀 خطوات التشغيل (Getting Started)

1. قم بعمل Clone للـ Repository:
   ```bash
   git clone [https://github.com/YOUR_USERNAME/REPO_NAME.git](https://github.com/YOUR_USERNAME/REPO_NAME.git)
