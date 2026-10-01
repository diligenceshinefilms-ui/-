package com.example.ui.localization

enum class AppLanguage {
  MARATHI,
  ENGLISH
}

object GramSevaStrings {
  fun appTitle(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "ग्रामसेवा" else "GramSeva"
  fun villageName(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "ग्रामपंचायत निचपूर" else "Gram Panchayat Nichpur"
  fun villageSubtitle(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "ता. किनवट, जि. नांदेड | आदर्श स्मार्ट व्हिलेज" else "Tal. Kinwat, Dist. Nanded | Smart Model Village"

  // Navigation
  fun tabHome(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "मुख्यपृष्ठ" else "Home"
  fun tabServices(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "सेवा व कर" else "Services"
  fun tabComplaints(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "तक्रार" else "Complaints"
  fun tabSabha(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "सभा व सूचना" else "Sabha & Notices"
  fun tabMore(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "अधिक माहिती" else "More & Info"

  // Home actions
  fun quickServicesTitle(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "नागरिक ई-सेवा" else "Citizen E-Services"
  fun btnFileComplaint(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "तक्रार नोंदवा" else "File Complaint"
  fun btnPayTax(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "कर व पाणीपट्टी" else "Pay Tax / Bills"
  fun btnCertificates(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "दाखले व अर्ज" else "Certificates"
  fun btnDevelopmentWorks(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "विकासकामे" else "Dev Works"
  fun btnGramSabha(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "ग्रामसभा" else "Gram Sabha"
  fun btnSchemes(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "शासकीय योजना" else "Gov Schemes"

  fun importantNotices(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "महत्त्वाच्या सूचना व जाहीर प्रगटन" else "Important Notices"
  fun viewAll(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "सर्व पहा" else "View All"
  fun keyOfficials(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "ग्रामपंचायत पदाधिकारी व अधिकारी" else "Panchayat Officials"
  fun emergencyContactsTitle(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "आपत्कालीन २४x७ संपर्क" else "Emergency 24x7 Contacts"

  // Complaints
  fun complaintsTitle(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "तक्रार निवारण प्रणाली" else "Grievance Redressal"
  fun searchComplaints(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "तक्रार क्रमांक किंवा विषयानुरूप शोधा..." else "Search by ID or subject..."
  fun registerNewComplaint(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "नवीन तक्रार नोंदणी" else "Register Complaint"
  fun statusAll(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "सर्व तक्रारी" else "All Complaints"
  fun statusActive(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "प्रगतीपथावर" else "In Progress"
  fun statusResolved(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "निवारण झाले" else "Resolved"

  // Complaint lifecycle stages
  fun stageSubmitted(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "दाखल झाली" else "Submitted"
  fun stageUnderReview(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "तपासणी सुरू" else "Under Review"
  fun stageAssigned(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "अधिकारी नियुक्त" else "Assigned"
  fun stageInProgress(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "काम सुरू" else "In Progress"
  fun stageResolved(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "निवारण झाले" else "Resolved"
  fun stageClosed(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "तक्रार बंद" else "Closed"

  // Services
  fun certificatesTab(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "दाखले व प्रमाणपत्रे" else "Certificates"
  fun billsTab(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "कर व पाणीपट्टी" else "Taxes & Bills"
  fun developmentTab(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "विकास प्रकल्प" else "Projects"
  fun applyCertificate(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "नवीन दाखला अर्ज करा" else "Apply Certificate"
  fun outstandingBills(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "थकीत कर व देयके" else "Outstanding Dues"
  fun paidBills(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "भरणा पावती इतिहास" else "Paid Receipts"
  fun payNow(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "आता भरा" else "Pay Now"
  fun viewReceipt(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "पावती पहा" else "Receipt"
  fun viewCertificate(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "दाखला पहा" else "View Certificate"

  // Admin
  fun adminPortal(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "ग्रामपंचायत अधिकारी प्रशासन" else "Officer Admin Portal"
  fun adminModeActive(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "प्रशासक मोड सुरू" else "Admin Mode Active"
  fun citizenMode(lang: AppLanguage) = if (lang == AppLanguage.MARATHI) "नागरिक मोड" else "Citizen Mode"
}
