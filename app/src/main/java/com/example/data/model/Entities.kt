package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "citizen_profile")
data class CitizenProfile(
  @PrimaryKey val id: String = "citizen_default",
  val fullName: String,
  val mobileNumber: String,
  val email: String,
  val address: String,
  val wardNumber: Int,
  val propertyNumber: String,
  val village: String = "निचपूर (Nichpur)",
  val taluka: String = "किनवट (Kinwat)",
  val district: String = "नांदेड (Nanded)",
  val isAdmin: Boolean = false
)

@Entity(tableName = "notices")
data class NoticeItem(
  @PrimaryKey val id: String,
  val titleMr: String,
  val titleEn: String,
  val category: String, // "सार्वजनिक", "पाणीपुरवठा", "कर वसुली", "आरोग्य", "योजना"
  val date: String,
  val priority: String, // "HIGH", "NORMAL"
  val descriptionMr: String,
  val descriptionEn: String,
  val attachmentName: String = "",
  val isPublished: Boolean = true
)

@Entity(tableName = "complaints")
data class ComplaintItem(
  @PrimaryKey val id: String,
  val complaintNumber: String,
  val category: String, // "पाणी पुरवठा", "रस्ते व गटारे", "दिवाबत्ती / स्ट्रीट लाइट", "स्वच्छता व कचरा", "आरोग्य सुविधा", "इतर"
  val subject: String,
  val description: String,
  val citizenName: String,
  val citizenMobile: String,
  val wardNumber: Int,
  val locationText: String,
  val status: String, // "SUBMITTED", "UNDER_REVIEW", "ASSIGNED", "IN_PROGRESS", "RESOLVED", "CLOSED"
  val submittedDate: String,
  val assignedOfficer: String = "",
  val officerRemark: String = "",
  val resolvedDate: String = "",
  val photoPlaceholder: String = "" // e.g. "streetlight_repair" or "water_leak"
)

@Entity(tableName = "bills")
data class BillItem(
  @PrimaryKey val id: String,
  val billNumber: String,
  val billType: String, // "घरपट्टी (Property Tax)", "पाणीपट्टी (Water Tax)", "दिवाबत्ती कर (Light Tax)"
  val consumerName: String,
  val propertyNumber: String,
  val assessmentYear: String,
  val amount: Double,
  val penaltyAmount: Double = 0.0,
  val dueDate: String,
  val isPaid: Boolean,
  val paidDate: String = "",
  val transactionId: String = "",
  val receiptNumber: String = ""
)

@Entity(tableName = "certificates")
data class CertificateApplication(
  @PrimaryKey val id: String,
  val applicationId: String,
  val certificateType: String, // "रहिवासी दाखला", "जन्म नोंद दाखला", "मृत्यू नोंद दाखला", "ना-हरकत प्रमाणपत्र (NOC)", "शौचालय प्रमाणपत्र"
  val applicantName: String,
  val relativeName: String,
  val idProofType: String, // "आधार कार्ड", "मतदान कार्ड", "रेशन कार्ड"
  val idProofNumber: String,
  val purpose: String,
  val status: String, // "PENDING", "APPROVED", "REJECTED"
  val appliedDate: String,
  val approvedDate: String = "",
  val remarks: String = "",
  val verificationCode: String = ""
)

@Entity(tableName = "development_works")
data class DevelopmentWork(
  @PrimaryKey val id: String,
  val titleMr: String,
  val titleEn: String,
  val category: String, // "रस्ते काँक्रीटीकरण", "पाणीपुरवठा", "सौर ऊर्जा", "शाळा व अंगणवाडी", "स्वच्छता"
  val location: String,
  val budgetAmount: String,
  val spentAmount: String,
  val status: String, // "APPROVED", "IN_PROGRESS", "COMPLETED", "PROPOSED"
  val progressPercent: Int,
  val contractorAgency: String,
  val startDate: String,
  val completionTargetDate: String,
  val workDetailsMr: String
)

@Entity(tableName = "gram_sabha")
data class GramSabhaMeeting(
  @PrimaryKey val id: String,
  val meetingDate: String,
  val meetingTime: String,
  val venueMr: String,
  val subjectMr: String,
  val agendaPoints: String, // Separated by '|'
  val isCompleted: Boolean,
  val resolutionsSummaryMr: String = "",
  val attendanceCount: Int = 0
)

@Entity(tableName = "schemes")
data class GovScheme(
  @PrimaryKey val id: String,
  val titleMr: String,
  val titleEn: String,
  val department: String,
  val benefitsMr: String,
  val eligibilityMr: String,
  val requiredDocsMr: String,
  val officialLink: String,
  val category: String
)

@Entity(tableName = "contacts")
data class EmergencyContact(
  @PrimaryKey val id: String,
  val designationMr: String,
  val designationEn: String,
  val personName: String,
  val department: String,
  val phoneNumber: String,
  val isEmergency: Boolean = false
)

@Entity(tableName = "diary_feed")
data class DailyDiaryFeed(
  @PrimaryKey val id: String,
  val date: String,
  val titleMr: String,
  val descriptionMr: String,
  val officerInCharge: String
)
