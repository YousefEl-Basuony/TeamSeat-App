# 📋 TeamSeat (StackMates) — لوحة الـ Agile الرسمية (4 شاشات لكل عضو = 24 شاشة)

> **قاعدة العمل الموحدة:**
> كل عضو في الفريق مسؤول عن **4 شاشات في فولدره الخاص** بنظام Jetpack Compose مع `@Preview`.
> لا يوجد أي كود سيرفر أو فايربيز داخل شاشات الـ UI، والبيانات التجريبية جاهزة في `MockDataProvider.kt`.

---

## 💻 1. مهام يوسف (Youssef - Team Lead & Backend) — `Screens/admin_youssef`
* [ ] **[YOU-1] `TASheetPreviewScreen.kt` (3 pts):** كشف التسليم الرسمي بنظام ورقة A4 بيضاء أنيقة بأسماء المشرفين (د. محمد كاظم وم. شهد أحمد).
* [ ] **[YOU-2] `LockTeamDialog.kt` (1 pt):** نافذة تحذيرية لاعتماد وإغلاق التيم نهائياً لمنع أي تعديل أو انسحاب.
* [ ] **[YOU-3] `DemoControlCenterScreen.kt` (2 pts):** لوحة تحكم سريعة للتبديل بين الحسابات وإعادة ضبط الكراسي للمناقشة.
* [ ] **[YOU-4] `EditTeamSeatsScreen.kt` (2 pts):** شاشة تمكن القائد من تعديل مسميات المقاعد المتاحة أو حذف كرسي فارغ.

---

## 🌸 2. مهام نُهى (Noha) — `Screens/auth_noha`
* [ ] **[NOHA-1] `SplashScreen.kt` (1 pt):** شاشة البداية واللوجو واسم التطبيق وعبارة ترحيبية.
* [ ] **[NOHA-2] `OnboardingScreen.kt` (3 pts):** سلايدر ترحيبي `HorizontalPager` من 3 صفحات لشرح فكرة حجز المقاعد.
* [ ] **[NOHA-3] `LoginScreen.kt` (2 pts):** فورم تسجيل الدخول (الإيميل/الرقم الجامعي + الباسورد + زر الدخول).
* [ ] **[NOHA-4] `RegisterScreen.kt` (3 pts):** استمارة تسجيل طالب جديد (الاسم، الكود، الإيميل، الباسورد، واختيار المهارات).

---

## 🔍 3. مهام خالد (Khaled) — `Screens/explore_khaled`
* [ ] **[KHALED-1] `HomeScreen.kt` (3 pts):** الصفحة الرئيسية واستعراض كروت التيمات والمقاعد العاجلة.
* [ ] **[KHALED-2] `SearchAndFilterScreen.kt` (3 pts):** شريط البحث وفلاتر التخصصات (Android, UI/UX...).
* [ ] **[KHALED-3] `CourseHubScreen.kt` (2 pts):** صفحة المادة الدراسية وتيماتها ونسب الاكتمال.
* [ ] **[KHALED-4] `TeamDetailsOverviewScreen.kt` (3 pts):** شاشة تفاصيل المشروع ووصف الفكرة قبل فتح خريطة الكراسي.

---

## ⚡ 4. مهام حسام (Hossam) — `Screens/requests_hossam`
* [ ] **[HOSSAM-1] `MyProfileScreen.kt` (2 pts):** شاشة ملفي الشخصي وتعديل المهارات ورابط GitHub وحالة الانضمام.
* [ ] **[HOSSAM-2] `StudentPublicProfileSheet.kt` (2 pts):** بطاقة منبثقة لمعاينة بروفايل أي طالب متقدم قبل قبوله.
* [ ] **[HOSSAM-3] `MyApplicationsScreen.kt` (3 pts):** متابعة طلباتي المعلقة (حد أقصى 3) مع عداد الـ 48 ساعة وسحب الطلب.
* [ ] **[HOSSAM-4] `NotificationsScreen.kt` (2 pts):** سجل الإشعارات الفورية الملونة (قبول، رفض، تنبيه).

---

## 💺 5. مهام إسراء (Esraa) — `Screens/seatmap_esraa`
* [ ] **[ESRAA-1] `InteractiveSeatMapScreen.kt` (5 pts):** خريطة المقاعد التفاعلية (كارت القائد 👑 + شبكة عمودين بالألوان 🟢🟡🔵).
* [ ] **[ESRAA-2] `CreateTeamInfoScreen.kt` (2 pts):** إنشاء تيم خطوة 1 (اسم المشروع والمادة والوصف وتحديد عدد المقاعد).
* [ ] **[ESRAA-3] `CreateTeamSeatsScreen.kt` (3 pts):** إنشاء تيم خطوة 2 (تخصيص مسمى وتخصص كل كرسي).
* [ ] **[ESRAA-4] `SeatBookingBottomSheet.kt` (1 pt):** نافذة حجز المقعد المتاح وكتابة رسالة سريعة للقائد.

---

## 📑 6. مهام آلاء (Alaa) — `Screens/workspace_alaa`
* [ ] **[ALAA-1] `TeamWorkspaceScreen.kt` (3 pts):** لوحة تحكم التيم المعتمد: كروت الأعضاء ونسبة الإنجاز وروابط GitHub و Drive.
* [ ] **[ALAA-2] `TeamGroupChatScreen.kt` (3 pts):** شاشة المحادثة الجماعية الداخلية للتيم.
* [ ] **[ALAA-3] `TeamCalendarScreen.kt` (2 pts):** جدول مواعيد التسليمات والمناقشات مع د. محمد كاظم وم. شهد.
* [ ] **[ALAA-4] `ReviewApplicationsScreen.kt` (3 pts):** شاشة القائد لمراجعة المتقدمين مع أزرار (قبول ✅ / رفض ❌).
