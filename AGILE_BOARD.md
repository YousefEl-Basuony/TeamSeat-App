# 📋 TeamSeat (StackMates) — Agile Task Board (توزيع الـ 6 أعضاء بالتساوي)

> **تشكيل التيم الكامل (6 أفراد):**
> * **فريق الـ UI (5 مطورين أندرويد Jetpack Compose — كل واحد 5 ملفات في فولدر مستقل تماماً):**
>   1. 🌸 **نُهى** (`Screens/auth_noha`)
>   2. 🔍 **خالد** (`Screens/explore_khaled`)
>   3. ⚡ **حسام** (`Screens/requests_hossam`)
>   4. 💺 **إسراء** (`Screens/seatmap_esraa`)
>   5. 📑 **آلاء** (`Screens/workspace_alaa`)
> * **الباك إند والربط (1 Backend & Team Lead):**
>   6. 💻 **أنت (يوسف)** (`data/repository`, `navigation`, `Firebase Auth & Firestore Batch Writes`, `PdfExporter`)

---

## 🔄 أعمدة لوحة الـ Agile (على Trello)

1. 📌 **To Do:** الشاشات الجاهزة كملفات فاضية في المشروع بانتظار التصميم.
2. ⏳ **In Progress:** الشاشة التي يصممها العضو حالياً.
3. 👁️ **In Review / Preview:** تم الانتهاء من تصميم الشاشة ورفع Push على الـ Branch.
4. ✅ **Done:** تمت المراجعة والربط في `AppNavGraph.kt`.

---

## 🌸 1. مهام نُهى (Noha) — فولدر `Screens/auth_noha` (5 شاشات)

* [ ] **[NOHA-1] `SplashScreen.kt` (1 pt):** شاشة البداية واللوجو واسم التطبيق.
* [ ] **[NOHA-2] `LoginScreen.kt` (2 pts):** حقل الإيميل/الرقم الجامعي + كلمة المرور + زر الدخول.
* [ ] **[NOHA-3] `RegisterScreen.kt` (3 pts):** استمارة تسجيل طالب جديد (الاسم، الرقم الجامعي، الإيميل، الباسورد).
* [ ] **[NOHA-4] `SetupProfileScreen.kt` (2 pts):** اختيار التخصص التقني عبر `FilterChips` + رابط GitHub.
* [ ] **[NOHA-5] `OnboardingScreen.kt` (3 pts):** سلايدر ترحيبي `HorizontalPager` من 3 صفحات يشرح فكرة حجز المقاعد.

---

## 🔍 2. مهام خالد (Khaled) — فولدر `Screens/explore_khaled` (5 شاشات)

* [ ] **[KHALED-1] `DemoSwitcherBar.kt` (1 pt):** شريط التبديل اللحظي وقت المناقشة بين (أحمد - ليدر) و(سارة - طالبة).
* [ ] **[KHALED-2] `CourseHubScreen.kt` (2 pts):** صفحة المادة الدراسية وعرض التيمات المسجلة تحتها.
* [ ] **[KHALED-3] `TeamDetailsOverviewScreen.kt` (3 pts):** شاشة تفاصيل وفكرة المشروع الشاملة مع زر فتح خريطة الكراسي.
* [ ] **[KHALED-4] `HomeScreen.kt` (3 pts):** الشاشة الرئيسية وتصفح التيمات المفتوحة والمقاعد العاجلة.
* [ ] **[KHALED-5] `SearchAndFilterScreen.kt` (3 pts):** البحث والفلترة حسب التخصص الشاغر أو اسم المادة.

---

## ⚡ 3. مهام حسام (Hossam) — فولدر `Screens/requests_hossam` (5 شاشات)

* [ ] **[HOSSAM-1] `MyProfileScreen.kt` (2 pts):** شاشة ملفي الشخصي، بياناتي الجامعية، وتعديل المهارات وروابط الأعمال.
* [ ] **[HOSSAM-2] `StudentPublicProfileSheet.kt` (2 pts):** بطاقة منبثقة لمعاينة بروفايل أي طالب متقدم.
* [ ] **[HOSSAM-3] `MyApplicationsScreen.kt` (3 pts):** متابعة طلباتي المعلقة (الحد الأقصى 3 طلبات) مع عداد الـ 48 ساعة وسحب الطلب.
* [ ] **[HOSSAM-4] `NotificationsScreen.kt` (2 pts):** مركز الإشعارات الفورية الملونة (قبول، طلب معلق، إغلاق التيم).
* [ ] **[HOSSAM-5] `ReviewApplicationsScreen.kt` (3 pts):** شاشة القائد لمراجعة المتقدمين لكل مقعد مع زري (قبول ✅ / رفض ❌).

---

## 💺 4. مهام إسراء (Esraa) — فولدر `Screens/seatmap_esraa` (5 شاشات)

* [ ] **[ESRAA-1] `SeatBookingBottomSheet.kt` (1 pt):** نافذة حجز المقعد المتاح وكتابة رسالة التقديم للقائد.
* [ ] **[ESRAA-2] `CreateTeamInfoScreen.kt` (2 pts):** الخطوة 1 لإنشاء التيم (بيانات المشروع واختيار عدد المقاعد 2–8).
* [ ] **[ESRAA-3] `CreateTeamSeatsScreen.kt` (3 pts):** الخطوة 2 لتخصيص مسمى وتخصص كل كرسي في التيم.
* [ ] **[ESRAA-4] `EditTeamSeatsScreen.kt` (2 pts):** تعديل مسميات المقاعد الفارغة قبل إغلاق التيم.
* [ ] **[ESRAA-5] `InteractiveSeatMapScreen.kt` (5 pts):** خريطة المقاعد التفاعلية بنظام السينما (كارت القائد بتاج 👑 + شبكة عمودين بألوان الحالات).

---

## 📑 5. مهام آلاء (Alaa) — فولدر `Screens/workspace_alaa` (5 شاشات)

* [ ] **[ALAA-1] `LockTeamDialog.kt` (1 pt):** نافذة تأكيد إغلاق واعتماد التيم نهائياً قبل تسليم الكشف.
* [ ] **[ALAA-2] `TeamCalendarScreen.kt` (2 pts):** جدول مواعيد التسليمات والمناقشات مع د. محمد كاظم وم. شهد.
* [ ] **[ALAA-3] `TeamGroupChatScreen.kt` (3 pts):** شاشة المحادثة الجماعية الداخلية لأعضاء التيم المعتمدين.
* [ ] **[ALAA-4] `TeamWorkspaceScreen.kt` (3 pts):** لوحة تحكم التيم المعتمد وروابط المشروع والأعضاء.
* [ ] **[ALAA-5] `TASheetPreviewScreen.kt` (3 pts):** تصميم ورقة A4 الرسمية لكشف تسليم التيم وتصدير PDF.

---

## 💻 6. مهامك أنت (Backend, Firebase & Integration) — فولدر `data` & `navigation`

* [x] **[BACKEND-1] إنشاء استراكشر المشروع والـ Models والـ `MockDataProvider` والـ `AppNavGraph`.**
* [ ] **[BACKEND-2] ربط `AuthRepository.kt` مع Firebase Auth.**
* [ ] **[BACKEND-3] ربط `TeamSeatRepository.kt` مع Cloud Firestore وتنفيذ الـ Batch Write للقبول وإلغاء باقي الطلبات.**
* [ ] **[BACKEND-4] كتابة قواعد حماية الـ Firestore Security Rules وفحص مهلة الـ 48 ساعة.**
* [ ] **[BACKEND-5] ربط `PdfExporter.kt` ومراجعة ودمج Pull Requests الفريق.**
