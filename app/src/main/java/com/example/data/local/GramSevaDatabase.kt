package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
  entities = [
    CitizenProfile::class,
    NoticeItem::class,
    ComplaintItem::class,
    BillItem::class,
    CertificateApplication::class,
    DevelopmentWork::class,
    GramSabhaMeeting::class,
    GovScheme::class,
    EmergencyContact::class,
    DailyDiaryFeed::class
  ],
  version = 2,
  exportSchema = false
)
abstract class GramSevaDatabase : RoomDatabase() {
  abstract fun citizenDao(): CitizenDao
  abstract fun noticeDao(): NoticeDao
  abstract fun complaintDao(): ComplaintDao
  abstract fun billDao(): BillDao
  abstract fun certificateDao(): CertificateDao
  abstract fun developmentWorkDao(): DevelopmentWorkDao
  abstract fun gramSabhaDao(): GramSabhaDao
  abstract fun schemeDao(): SchemeDao
  abstract fun contactDao(): ContactDao
  abstract fun diaryDao(): DiaryDao

  companion object {
    @Volatile
    private var INSTANCE: GramSevaDatabase? = null

    fun getDatabase(context: Context): GramSevaDatabase {
      return INSTANCE ?: synchronized(this) {
        val instance = Room.databaseBuilder(
          context.applicationContext,
          GramSevaDatabase::class.java,
          "gramseva_nichpur_v2.db"
        )
        .addCallback(DatabaseCallback())
        .build()
        INSTANCE = instance
        instance
      }
    }
  }

  private class DatabaseCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
      super.onCreate(db)
      INSTANCE?.let { database ->
        CoroutineScope(Dispatchers.IO).launch {
          seedInitialData(database)
        }
      }
    }
  }
}

suspend fun seedInitialData(database: GramSevaDatabase) {
  // 1. Citizen Profile - अशोक घारगे (Ashok Gharge)
  database.citizenDao().insertCitizen(
    CitizenProfile(
      id = "citizen_default",
      fullName = "अशोक घारगे",
      mobileNumber = "9422114589",
      email = "ashok.gharge94@gmail.com",
      address = "घर नं. १२४, मेन रोड, प्रभाग क्र. २",
      wardNumber = 2,
      propertyNumber = "GP/NCH/2026/0124",
      village = "निचपूर (Nichpur)",
      taluka = "किनवट (Kinwat)",
      district = "नांदेड (Nanded)",
      isAdmin = false
    )
  )

  // 2. Notices - निचपूर, ता. किनवट, जि. नांदेड
  database.noticeDao().insertAll(
    listOf(
      NoticeItem(
        id = "1",
        titleMr = "जल जीवन मिशन निचपूर: पाईपलाईन दुरुस्ती व पाणीपुरवठा वेळ बदल",
        titleEn = "Pipeline Maintenance: Water Supply Schedule Update",
        category = "पाणीपुरवठा",
        date = "०१ ऑक्टोबर २०२६",
        priority = "HIGH",
        descriptionMr = "किनवट मुख्य जलवाहिनी दुरुस्ती व निचपूर जलशुद्धीकरण प्लांट स्वच्छता कामामुळे उद्या प्रभाग १ ते ४ मधील पाणीपुरवठा सकाळी ६:३० ते ८:३० या वेळेतच राहील. ग्रामस्थांनी पाणी साठवून सहकार्य करावे. - महेश मुंडे (ग्रामसेवक)",
        descriptionEn = "Due to mainline repairs and filtration cleaning, water supply across Nichpur wards will operate from 6:30 AM to 8:30 AM tomorrow. Citizens are requested to store adequate water.",
        attachmentName = "Water_Schedule_Nichpur.pdf"
      ),
      NoticeItem(
        id = "2",
        titleMr = "ग्रामपंचायत निचपूर कर सवलत योजना: १५% त्वरित सूट",
        titleEn = "Panchayat Tax Rebate Scheme: 15% Early Bird Discount",
        category = "कर वसुली",
        date = "२८ सप्टेंबर २०२६",
        priority = "NORMAL",
        descriptionMr = "सन २०२६-२७ ची घरपट्टी व पाणीपट्टी ३१ ऑक्टोबर पूर्वी भरणाऱ्या निचपूर ग्रामस्थांना १५% सवलत दिली जाईल. ग्रामसेवा ॲपवरून विनाशुल्क त्वरित कर भरणा करावा. - सदाशिव मुंडे (सरपंच)",
        descriptionEn = "Citizens paying property and water tax before 31st October will receive a 15% rebate. Pay seamlessly through the GramSeva app.",
        attachmentName = "Tax_Rebate_Notice_Nichpur.pdf"
      ),
      NoticeItem(
        id = "3",
        titleMr = "मोफत आरोग्य तपासणी शिबीर - प्राथमिक आरोग्य केंद्र किनवट",
        titleEn = "Free Health and Eye Checkup Camp Kinwat",
        category = "आरोग्य",
        date = "२५ सप्टेंबर २०२६",
        priority = "NORMAL",
        descriptionMr = "प्राथमिक आरोग्य केंद्र व जिल्हा रुग्णालय नांदेड यांच्या संयुक्त विद्यमाने मोफत मधुमेह, रक्तदाब व नेत्र तपासणी शिबीर रविवारी सकाळी १० वाजता समाजमंदिर निचपूर येथे आयोजित केले आहे.",
        descriptionEn = "Free diabetes, blood pressure, and cataract screening camp this Sunday at 10 AM at Samaj Mandir in collaboration with District Hospital Nanded.",
        attachmentName = "Health_Camp_Guidelines.pdf"
      ),
      NoticeItem(
        id = "4",
        titleMr = "प्रधानमंत्री किसान सन्मान निधी ई-केवायसी अनिवार्य सूचना",
        titleEn = "PM-Kisan 16th Installment e-KYC Mandatory Notice",
        category = "योजना",
        date = "२० सप्टेंबर २०२६",
        priority = "HIGH",
        descriptionMr = "किनवट तालुक्यातील सर्व शेतकरी बांधवांनी पीएम किसान खात्याचे आधार बायोमेट्रिक ई-केवायसी ग्रामपंचायत निचपूर आपले सरकार सेवा केंद्रात तात्काळ पूर्ण करून घ्यावे.",
        descriptionEn = "All farmers must complete biometric e-KYC for PM-Kisan accounts at the Gram Panchayat Nichpur Aaple Sarkar Seva Kendra.",
        attachmentName = "PM_Kisan_Instructions.pdf"
      )
    )
  )

  // 3. Complaints - Lodged by sample persons
  database.complaintDao().insertAll(
    listOf(
      ComplaintItem(
        id = "1",
        complaintNumber = "GP-NCH-2026-0842",
        category = "दिवाबत्ती / स्ट्रीट लाइट",
        subject = "मेन रोडवरील मारुती मंदिराशेजारील ५० वॅट एलईडी पथदिवा बंद आहे",
        description = "गेल्या चार दिवसांपासून प्रभाग २ मधील मुख्य चौकातील पथदिवा बंद असल्याने रात्रीच्या वेळी अंधार असतो व ग्रामस्थांना ये-जा करताना त्रास होतो.",
        citizenName = "अशोक घारगे",
        citizenMobile = "9422114589",
        wardNumber = 2,
        locationText = "मेन रोड मारुती मंदिर चौक, निचपूर",
        status = "RESOLVED",
        submittedDate = "२४ सप्टेंबर २०२६",
        assignedOfficer = "श्री. महेश मुंडे (ग्रामसेवक)",
        officerRemark = "नवीन ५० वॅट एलईडी फिटिंग बसवून दुरुस्ती पूर्ण केली आहे. पथदिवा पूर्ववत सुरू आहे. - महेश मुंडे",
        resolvedDate = "२६ सप्टेंबर २०२६",
        photoPlaceholder = "streetlight"
      ),
      ComplaintItem(
        id = "2",
        complaintNumber = "GP-NCH-2026-0855",
        category = "रस्ते व गटारे",
        subject = "निचपूर ते किनवट मुख्य रस्त्यावरील खड्डे व मुरूम भराव",
        description = "पावसामुळे निचपूर-किनवट मुख्य पोहोच रस्त्यावर खड्डे पडल्याने वाहतूक संथ झाली आहे. त्वरित खडी-मुरूम टाकून सपाटीकरण करावे.",
        citizenName = "सदाशिव मुंडे",
        citizenMobile = "9422011234",
        wardNumber = 1,
        locationText = "निचपूर-किनवट मुख्य पोहोच रस्ता",
        status = "IN_PROGRESS",
        submittedDate = "२८ सप्टेंबर २०२६",
        assignedOfficer = "श्री. पंकज पाटील (उपसरपंच)",
        officerRemark = "मुरूम व खडी भराव चालू असून सार्वजनिक बांधकाम उपविभाग किनवट यांच्या सहकार्याने काम पूर्ण केले जाईल.",
        resolvedDate = "",
        photoPlaceholder = "road_patch"
      ),
      ComplaintItem(
        id = "3",
        complaintNumber = "GP-NCH-2026-0861",
        category = "पाणी पुरवठा",
        subject = "हनुमान गल्ली निचपूर सार्वजनिक नळजोडणीस कमी दाबाने पाणी",
        description = "हनुमान गल्लीतील शेवटच्या ५ घरांपर्यंत कमी दाबाने पाणी पोहोचते, व्हॉल्व्ह तपासणी करून सुरळीत करावे.",
        citizenName = "पंकज पाटील",
        citizenMobile = "9823456789",
        wardNumber = 3,
        locationText = "हनुमान गल्ली, घर क्र. ५५ ते ६५, निचपूर",
        status = "UNDER_REVIEW",
        submittedDate = "३० सप्टेंबर २०२६",
        assignedOfficer = "श्री. महेश मुंडे (ग्रामसेवक)",
        officerRemark = "पाणीपुरवठा कर्मचारी व वायरमन यांना व्हॉल्व्ह व एअर-लॉक तपासणीचे आदेश दिले आहेत.",
        resolvedDate = "",
        photoPlaceholder = "water_pipe"
      )
    )
  )

  // 4. Bills for अशोक घारगे
  database.billDao().insertAll(
    listOf(
      BillItem(
        id = "1",
        billNumber = "BILL-PROP-2026-124",
        billType = "घरपट्टी (Property Tax)",
        consumerName = "अशोक घारगे",
        propertyNumber = "GP/NCH/2026/0124",
        assessmentYear = "२०२६ - २०२७",
        amount = 1450.0,
        penaltyAmount = 0.0,
        dueDate = "३१ ऑक्टोबर २०२६",
        isPaid = false
      ),
      BillItem(
        id = "2",
        billNumber = "BILL-WTR-2026-124",
        billType = "पाणीपट्टी (Water Tax)",
        consumerName = "अशोक घारगे",
        propertyNumber = "GP/NCH/2026/0124",
        assessmentYear = "२०२६ - २०२७",
        amount = 900.0,
        penaltyAmount = 0.0,
        dueDate = "३१ ऑक्टोबर २०२६",
        isPaid = false
      ),
      BillItem(
        id = "3",
        billNumber = "BILL-LGT-2025-124",
        billType = "दिवाबत्ती कर (Light Tax)",
        consumerName = "अशोक घारगे",
        propertyNumber = "GP/NCH/2026/0124",
        assessmentYear = "२०२५ - २०२६",
        amount = 250.0,
        penaltyAmount = 0.0,
        dueDate = "३१ मार्च २०२६",
        isPaid = true,
        paidDate = "१५ मे २०२६",
        transactionId = "TXN99824102914",
        receiptNumber = "GP-NCH-RCP-2025-1048"
      )
    )
  )

  // 5. Certificates - अशोक घारगे & पंकज पाटील
  database.certificateDao().insertAll(
    listOf(
      CertificateApplication(
        id = "1",
        applicationId = "CRT-NCH-2026-092",
        certificateType = "रहिवासी दाखला (Residence Certificate)",
        applicantName = "अशोक घारगे",
        relativeName = "विठ्ठलराव घारगे (वडील)",
        idProofType = "आधार कार्ड",
        idProofNumber = "XXXX-XXXX-8921",
        purpose = "महाविद्यालयीन शिष्यवृत्ती व शासकीय योजनेसाठी",
        status = "APPROVED",
        appliedDate = "१२ सप्टेंबर २०२६",
        approvedDate = "१४ सप्टेंबर २०२६",
        remarks = "सर्व कागदपत्रांची पडताळणी पूर्ण झाली असून दाखला स्वाक्षरीसह वितरित केला आहे. - सदाशिव मुंडे (सरपंच)",
        verificationCode = "GP-NCH-RES-2026-092"
      ),
      CertificateApplication(
        id = "2",
        applicationId = "CRT-NCH-2026-118",
        certificateType = "ना-हरकत प्रमाणपत्र (NOC - वीज जोडणी)",
        applicantName = "पंकज पाटील",
        relativeName = "दत्तात्रय पाटील",
        idProofType = "७/१२ उतारा व घरपट्टी पावती",
        idProofNumber = "७/१२ गट क्र. ९४/१, निचपूर",
        purpose = "महावितरण शेती थ्री-फेज पंप वीज जोडणीसाठी",
        status = "PENDING",
        appliedDate = "२९ सप्टेंबर २०२६",
        approvedDate = "",
        remarks = "अर्ज ग्रामसेवक महेश मुंडे यांच्या कार्यालयात पडताळणीसाठी प्रलंबित आहे.",
        verificationCode = "GP-NCH-NOC-2026-118"
      )
    )
  )

  // 6. Development Works - ग्रामपंचायत निचपूर (ता. किनवट, जि. नांदेड)
  database.developmentWorkDao().insertAll(
    listOf(
      DevelopmentWork(
        id = "1",
        titleMr = "निचपूर मुख्य रस्ता काँक्रीटीकरण व बंदिस्त भूमिगत गटार योजना",
        titleEn = "Nichpur Main Road Concrete Pavement and Underground Drainage",
        category = "रस्ते व गटारे",
        location = "ग्रामपंचायत कार्यालय ते मारुती मंदिर (८५० मीटर, निचपूर)",
        budgetAmount = "₹ २५,५०,०००",
        spentAmount = "₹ २१,८०,०००",
        status = "IN_PROGRESS",
        progressPercent = 85,
        contractorAgency = "किरण कन्स्ट्रक्शन, किनवट",
        startDate = "०१ जून २०२६",
        completionTargetDate = "१५ नोव्हेंबर २०२६",
        workDetailsMr = "सिमेंट काँक्रीटीकरण ८५% पूर्ण झाले असून दोन्ही बाजूंनी पेव्हर ब्लॉक व गटार स्लॅब आच्छादन काम अंतिम टप्प्यात आहे."
      ),
      DevelopmentWork(
        id = "2",
        titleMr = "जल जीवन मिशन - निचपूर हर घर जल शुद्ध नळ पाणीपुरवठा योजना",
        titleEn = "Jal Jeevan Mission - Har Ghar Jal Tap Water Connectivity",
        category = "पाणीपुरवठा",
        location = "संपूर्ण निचपूर गाव (प्रभाग १ ते ४)",
        budgetAmount = "₹ १८,००,०००",
        spentAmount = "₹ १८,००,०००",
        status = "COMPLETED",
        progressPercent = 100,
        contractorAgency = "महाराष्ट्र जीवन प्राधिकरण, उपविभाग किनवट",
        startDate = "१५ जानेवारी २०२६",
        completionTargetDate = "३० ऑगस्ट २०२६",
        workDetailsMr = "सर्व कुटुंबांना वैयक्तिक नळ जोडणी, जलशुद्धीकरण केंद्र (RO प्लांट) व नवीन ५०,००० लिटर उंच पाण्याची टाकी कार्यान्वित."
      ),
      DevelopmentWork(
        id = "3",
        titleMr = "५० किलोवॅट सोलर रुफटॉप ग्रीड सिस्टीम (हरित ऊर्जा निचपूर)",
        titleEn = "50 kW Solar Rooftop On-Grid Power Plant for Panchayat",
        category = "सौर ऊर्जा",
        location = "ग्रामपंचायत इमारत व पाणी उपसा केंद्र, निचपूर",
        budgetAmount = "₹ १२,००,०००",
        spentAmount = "₹ १०,५०,०००",
        status = "IN_PROGRESS",
        progressPercent = 90,
        contractorAgency = "महाऊर्जा (MEDA) अधिकृत एजन्सी, नांदेड",
        startDate = "१० जुलै २०२६",
        completionTargetDate = "२० ऑक्टोबर २०२६",
        workDetailsMr = "सोलर पॅनेल्स बसवले असून महावितरण नेट मीटरिंग जोडणीची अंतिम चाचणी बाकी आहे. यामुळे ग्रामपंचायतीचे वार्षिक ₹४ लाखांचे वीजबिल वाचेल."
      ),
      DevelopmentWork(
        id = "4",
        titleMr = "डिजिटल जि. प. प्राथमिक शाळा निचपूर व अभ्यासिका नूतनीकरण",
        titleEn = "Digital Smart ZP Primary School and Public Study Center",
        category = "शिक्षण व समाजकल्याण",
        location = "जि. प. प्राथमिक शाळा निचपूर (ता. किनवट)",
        budgetAmount = "₹ ६,००,०००",
        spentAmount = "₹ ६,००,०००",
        status = "COMPLETED",
        progressPercent = 100,
        contractorAgency = "ग्रामपंचायत स्वनिधी व लोकसहभाग",
        startDate = "०१ मार्च २०२६",
        completionTargetDate = "१० जून २०२६",
        workDetailsMr = "स्मार्ट इंटरॅक्टिव्ह टीव्ही, स्पर्धा परीक्षा अभ्यासिका, संगणक लॅब व मुलामुलींसाठी स्वतंत्र स्वच्छतागृहे पूर्ण."
      )
    )
  )

  // 7. Gram Sabha - निचपूर
  database.gramSabhaDao().insertAll(
    listOf(
      GramSabhaMeeting(
        id = "1",
        meetingDate = "१५ ऑक्टोबर २०२६",
        meetingTime = "सकाळी १०:०० वाजता",
        venueMr = "छत्रपती शिवाजी महाराज समाजमंदिर सभागृह, निचपूर (ता. किनवट)",
        subjectMr = "गांधी जयंती व विशेष ग्रामसभा व विकास आढावा बैठक",
        agendaPoints = "मागील सभेचे इतिवृत्त वाचन व मंजुरी (श्री. महेश मुंडे)|सन २०२६-२७ च्या पहिल्या सहामाहीचे जमाखर्च वाचन|पंतप्रधान आवास योजनेची पात्र लाभार्थ्यांची नवीन यादी वाचन (श्री. सदाशिव मुंडे)|गाव स्वच्छता व कचरा व्यवस्थापन मोहीम नियोजन (श्री. पंकज पाटील)|ग्रामस्थांच्या ऐनवेळच्या विषयांवर चर्चा (श्री. अशोक घारगे)",
        isCompleted = false,
        attendanceCount = 0
      ),
      GramSabhaMeeting(
        id = "2",
        meetingDate = "०१ मे २०२६",
        meetingTime = "सकाळी ०९:३० वाजता",
        venueMr = "ग्रामपंचायत कार्यालय प्रांगण, निचपूर",
        subjectMr = "महाराष्ट्र दिन विशेष ग्रामसभा व पाणीटंचाई आढावा",
        agendaPoints = "ग्रामपंचायत अंदाजपत्रक सन २०२६-२७ मंजुरी|पाणीपुरवठा नियोजन व विहिरीतील गाळ काढणे|शाळा पोषण आहार व अंगणवाडी तपासणी अहवाल|महिला बचत गट प्रोत्साहन ठराव",
        isCompleted = true,
        resolutionsSummaryMr = "अंदाजपत्रकास सर्वानुमते मंजुरी देण्यात आली. सरपंच सदाशिव मुंडे व ग्रामसेवक महेश मुंडे यांच्या उपस्थितीत ठराव क्र. ४ मंजूर झाला.",
        attendanceCount = 285
      )
    )
  )

  // 8. Schemes
  database.schemeDao().insertAll(
    listOf(
      GovScheme(
        id = "1",
        titleMr = "प्रधानमंत्री किसान सन्मान निधी (PM-KISAN)",
        titleEn = "Pradhan Mantri Kisan Samman Nidhi",
        department = "कृषी व शेतकरी कल्याण मंत्रालय",
        benefitsMr = "सर्व पात्र शेतकरी कुटुंबांना दरवर्षी ₹६,०००/- ची आर्थिक मदत थेट बँक खात्यात तीन समान हप्त्यांमध्ये (₹२,००० प्रत्येकी).",
        eligibilityMr = "स्वतःच्या नावावर शेतजमीन असणारे सर्व लहान व सीमांत शेतकरी कुटुंब.",
        requiredDocsMr = "आधार कार्ड, बँक पासबुक (आधार लिंक), ७/१२ व ८-अ उतारा, मोबाईल नंबर.",
        officialLink = "https://pmkisan.gov.in",
        category = "कृषी"
      ),
      GovScheme(
        id = "2",
        titleMr = "प्रधानमंत्री आवास योजना (ग्रामीण)",
        titleEn = "Pradhan Mantri Awas Yojana (Gramin)",
        department = "ग्रामविकास मंत्रालय, भारत सरकार",
        benefitsMr = "पक्के घर बांधकामासाठी थेट ₹१,२०,०००/- अनुदान व मनरेगा अंतर्गत ९० दिवसांची मजुरी आणि शौचालय बांधकामासाठी ₹१२,०००/-.",
        eligibilityMr = "कच्चे घर किंवा बेघर असणारी ग्रामपंचायतीच्या SECC यादीतील पात्र कुटुंबे.",
        requiredDocsMr = "आधार कार्ड, जॉब कार्ड, बँक खाते पासबुक, जागेचा ७/१२ किंवा मालकी पुरावा.",
        officialLink = "https://pmayg.nic.in",
        category = "गृहनिर्माण"
      ),
      GovScheme(
        id = "3",
        titleMr = "मुख्यमंत्री माझी लाडकी बहीण योजना",
        titleEn = "Mukhyamantri Majhi Ladki Bahin Yojana",
        department = "महिला व बालविकास विभाग, महाराष्ट्र शासन",
        benefitsMr = "पात्र महिलांना दरमहा ₹१,५००/- थेट बँक खात्यात आर्थिक स्वावलंबनासाठी डीबीटी द्वारे जमा.",
        eligibilityMr = "महाराष्ट्रातील २१ ते ६५ वयोगटातील विवाहित, विधवा, घटस्फोटित, परित्यक्त्या व निराधार महिला. कुटुंबाचे वार्षिक उत्पन्न २.५ लाखांपेक्षा कमी.",
        requiredDocsMr = "आधार कार्ड, अधिवास प्रमाणपत्र (डोमिसाइल/रहिवासी), रेशन कार्ड, बँक पासबुक, हमीपत्र.",
        officialLink = "https://ladkibahin.maharashtra.gov.in",
        category = "महिला व बालविकास"
      ),
      GovScheme(
        id = "4",
        titleMr = "संजय गांधी निराधार अनुदान योजना",
        titleEn = "Sanjay Gandhi Niradhar Anudan Yojana",
        department = "सामाजिक न्याय व विशेष सहाय्य विभाग",
        benefitsMr = "निराधार व्यक्ती, अंध, अपंग, अनाथ मुले व दुर्धर आजारग्रस्त व्यक्तींना दरमहा ₹१,५००/- पेन्शन.",
        eligibilityMr = "६५ वर्षांखालील निराधार, अपंगत्व ४०% पेक्षा जास्त, वार्षिक कौटुंबिक उत्पन्न ₹२१,००० पेक्षा कमी.",
        requiredDocsMr = "वयाचा पुरावा, उत्पन्नाचा दाखला, अपंगत्व प्रमाणपत्र, रहिवासी दाखला.",
        officialLink = "https://sjsa.maharashtra.gov.in",
        category = "सामाजिक न्याय"
      )
    )
  )

  // 9. Emergency Contacts - Featuring the Sample Persons: सदाशिव मुंडे, पंकज पाटील, महेश मुंडे, अशोक घारगे
  database.contactDao().insertAll(
    listOf(
      EmergencyContact(
        id = "1",
        designationMr = "सरपंच",
        designationEn = "Sarpanch",
        personName = "मा. श्री. सदाशिव मुंडे",
        department = "ग्रामपंचायत निचपूर (ता. किनवट, जि. नांदेड)",
        phoneNumber = "9422011234",
        isEmergency = true
      ),
      EmergencyContact(
        id = "2",
        designationMr = "उपसरपंच",
        designationEn = "Deputy Sarpanch",
        personName = "श्री. पंकज पाटील",
        department = "ग्रामपंचायत निचपूर",
        phoneNumber = "9823456789",
        isEmergency = false
      ),
      EmergencyContact(
        id = "3",
        designationMr = "ग्रामसेवक / ग्रामविकास अधिकारी",
        designationEn = "Gramsevak / Village Development Officer",
        personName = "श्री. महेश मुंडे",
        department = "ग्रामपंचायत कार्यालय निचपूर",
        phoneNumber = "9850123456",
        isEmergency = true
      ),
      EmergencyContact(
        id = "4",
        designationMr = "तलाठी / महसूल अधिकारी",
        designationEn = "Talathi (Revenue Officer)",
        personName = "श्री. अशोक घारगे",
        department = "महसूल विभाग, किनवट (सझा निचपूर)",
        phoneNumber = "9403124567",
        isEmergency = false
      ),
      EmergencyContact(
        id = "5",
        designationMr = "वैद्यकीय अधिकारी (प्राथमिक आरोग्य केंद्र)",
        designationEn = "Medical Officer (PHC Kinwat)",
        personName = "डॉ. सचिन देसाई (MBBS)",
        department = "प्राथमिक आरोग्य केंद्र किनवट",
        phoneNumber = "02469222100",
        isEmergency = true
      ),
      EmergencyContact(
        id = "6",
        designationMr = "१०८ शासकीय रुग्णवाहिका (किनवट कक्ष)",
        designationEn = "108 Govt Ambulance Service",
        personName = "आपत्कालीन रुग्णवाहिका नियंत्रण कक्ष किनवट",
        department = "महाराष्ट्र शासन आरोग्य विभाग",
        phoneNumber = "108",
        isEmergency = true
      ),
      EmergencyContact(
        id = "7",
        designationMr = "महावितरण वीज तक्रार (MSEDCL किनवट)",
        designationEn = "MSEDCL Electricity Substation",
        personName = "सहाय्यक अभियंता कार्यालय",
        department = "महावितरण उपकेंद्र किनवट ग्रामीण",
        phoneNumber = "1912",
        isEmergency = true
      ),
      EmergencyContact(
        id = "8",
        designationMr = "पोलीस ठाणे किनवट (नांदेड पोलीस)",
        designationEn = "Police Station (Kinwat, Nanded)",
        personName = "पोलीस निरीक्षक कार्यालय किनवट",
        department = "महाराष्ट्र पोलीस, जि. नांदेड",
        phoneNumber = "112",
        isEmergency = true
      )
    )
  )

  // 10. Daily Diary Feed - निचपूर
  database.diaryDao().insertAll(
    listOf(
      DailyDiaryFeed(
        id = "1",
        date = "०१ ऑक्टोबर २०२६",
        titleMr = "स्वच्छ भारत अभियानांतर्गत निचपूर प्रभाग २ मध्ये विशेष मोहीम",
        descriptionMr = "गांधी जयंती पूर्वसंध्येला सरपंच सदाशिव मुंडे, ग्रामसेवक महेश मुंडे, उपसरपंच पंकज पाटील व अशोक घारगे यांच्या उपस्थितीत गावातून प्रभातफेरी व स्वच्छता मोहीम पूर्ण करण्यात आली.",
        officerInCharge = "श्री. महेश मुंडे (ग्रामसेवक)"
      ),
      DailyDiaryFeed(
        id = "2",
        date = "३० सप्टेंबर २०२६",
        titleMr = "जल जीवन मिशन निचपूर पाण्याच्या नमुन्यांची रासायनिक चाचणी",
        descriptionMr = "आरोग्य विभागाच्या प्रयोगशाळा तंत्रज्ञांनी निचपूर पाण्याच्या मुख्य टाकीतील पाण्याचे नमुने तपासले. पाणी पिण्यासाठी १००% सुरक्षित असल्याचे निष्पन्न.",
        officerInCharge = "श्री. महेश मुंडे व आरोग्य पथक"
      ),
      DailyDiaryFeed(
        id = "3",
        date = "२७ सप्टेंबर २०२६",
        titleMr = "ग्रामपंचायत निचपूर इमारत सोलर ऊर्जेवर कार्यान्वित",
        descriptionMr = "५० किलोवॅट सोलर प्लांटचे इनव्हर्टर व ग्रीड चाचणी यशस्वी. ग्रामपंचायत कार्यालयाची सर्व उपकरणे आता सोलर ऊर्जेवर कार्यरत.",
        officerInCharge = "मा. सरपंच सदाशिव मुंडे व महाऊर्जा अभियंता"
      )
    )
  )
}
