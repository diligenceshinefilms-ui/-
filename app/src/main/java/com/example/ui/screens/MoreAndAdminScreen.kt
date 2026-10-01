package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ComplaintItem
import com.example.data.model.EmergencyContact
import com.example.data.model.GovScheme
import com.example.ui.components.GramPanchayatEmblem
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.GramSevaStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.GramSevaUiState

@Composable
fun MoreAndAdminScreen(
  uiState: GramSevaUiState,
  onToggleAdmin: () -> Unit,
  onOpenAdminComplaintUpdate: (ComplaintItem) -> Unit,
  onAdminApproveCert: (String, String) -> Unit,
  onAdminRejectCert: (String, String) -> Unit,
  onAdminOpenNotice: () -> Unit,
  onAdminUpdateProgress: (String, Int, String) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val lang = uiState.language
  val citizen = uiState.citizen
  var selectedTab by remember { mutableStateOf(if (uiState.isAdminMode) 2 else 0) } // 0: Profile & Village, 1: Schemes & Contacts, 2: Admin Portal

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = 16.dp),
    verticalArrangement = Arrangement.spacedBy(14.dp)
  ) {
    // Header
    Row(
      modifier = Modifier.fillMaxWidth(),
      horizontalArrangement = Arrangement.SpaceBetween,
      verticalAlignment = Alignment.CenterVertically
    ) {
      Column {
        Text(
          text = if (lang == AppLanguage.MARATHI) "अधिक माहिती व प्रशासन" else "Info & Admin",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "गाव माहिती, शासकीय योजना व अधिकारी कक्ष",
          fontSize = 11.sp,
          color = TextSecondary
        )
      }

      // Mode Switch Pill
      Surface(
        shape = RoundedCornerShape(12.dp),
        color = if (uiState.isAdminMode) SaffronContainer else GreenContainer,
        modifier = Modifier.clickable { onToggleAdmin() }
      ) {
        Row(
          modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Icon(
            imageVector = if (uiState.isAdminMode) Icons.Default.AdminPanelSettings else Icons.Default.Person,
            contentDescription = null,
            tint = if (uiState.isAdminMode) OnSaffronContainer else GreenPrimary,
            modifier = Modifier.size(16.dp)
          )
          Text(
            text = if (uiState.isAdminMode) "प्रशासक" else "नागरिक",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = if (uiState.isAdminMode) OnSaffronContainer else GreenPrimary
          )
        }
      }
    }

    // Sub Tabs
    Surface(
      shape = RoundedCornerShape(12.dp),
      color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
      modifier = Modifier.fillMaxWidth()
    ) {
      Row(
        modifier = Modifier
          .fillMaxWidth()
          .padding(4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
      ) {
        SubTabPill(
          title = if (lang == AppLanguage.MARATHI) "माझे प्रोफाईल" else "Profile",
          icon = Icons.Default.Person,
          isSelected = selectedTab == 0,
          onClick = { selectedTab = 0 },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = if (lang == AppLanguage.MARATHI) "योजना व संपर्क" else "Schemes",
          icon = Icons.Default.Stars,
          isSelected = selectedTab == 1,
          onClick = { selectedTab = 1 },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = if (lang == AppLanguage.MARATHI) "प्रशासन पोर्टल" else "Admin Portal",
          icon = Icons.Default.AdminPanelSettings,
          isSelected = selectedTab == 2,
          onClick = { selectedTab = 2 },
          modifier = Modifier.weight(1f)
        )
      }
    }

    when (selectedTab) {
      0 -> CitizenProfileContent(citizen = citizen, lang = lang)
      1 -> SchemesAndContactsContent(
        schemes = uiState.schemes,
        contacts = uiState.emergencyContacts,
        lang = lang,
        onCallContact = { phone ->
          context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone")))
        }
      )
      2 -> AdminPortalContent(
        uiState = uiState,
        lang = lang,
        onUpdateComplaint = onOpenAdminComplaintUpdate,
        onApproveCert = onAdminApproveCert,
        onRejectCert = onAdminRejectCert,
        onOpenNotice = onAdminOpenNotice,
        onUpdateWork = onAdminUpdateProgress
      )
    }
  }
}

// 1. Citizen Profile
@Composable
fun CitizenProfileContent(
  citizen: com.example.data.model.CitizenProfile?,
  lang: AppLanguage
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
          ) {
            Box(
              modifier = Modifier
                .size(54.dp)
                .clip(CircleShape)
                .background(GreenContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(32.dp))
            }
            Column {
              Text(
                text = citizen?.fullName ?: "अशोक घारगे",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "मोबाईल: +९१ ${citizen?.mobileNumber ?: "9422114589"}",
                fontSize = 12.sp,
                color = TextSecondary
              )
            }
          }

          HorizontalDivider()

          ProfileRow("पत्ता:", citizen?.address ?: "घर नं. १२४, मेन रोड, प्रभाग क्र. २")
          ProfileRow("प्रभाग क्र.:", "प्रभाग क्र. ${citizen?.wardNumber ?: 2}")
          ProfileRow("मालमत्ता क्र.:", citizen?.propertyNumber ?: "GP/NCH/2026/0124")
          ProfileRow("गाव:", citizen?.village ?: "निचपूर (Nichpur)")
          ProfileRow("तालुका व जिल्हा:", "${citizen?.taluka ?: "किनवट"} | ${citizen?.district ?: "नांदेड"}")
        }
      }
    }

    // Village Amenities Info Card
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenContainer.copy(alpha = 0.5f))
      ) {
        Column(
          modifier = Modifier.padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
          ) {
            GramPanchayatEmblem(size = 32)
            Text(
              text = "निचपूर गाव सार्वजनिक सुविधा (ता. किनवट, जि. नांदेड)",
              fontSize = 14.sp,
              fontWeight = FontWeight.Bold,
              color = OnGreenContainer
            )
          }

          val amenities = listOf(
            "• २४x७ शुद्ध पेयजल (RO फिल्टर प्लांट व हर घर जल जोडणी)",
            "• १००% गावात भूमिगत बंदिस्त सांडपाणी गटार व्यवस्था",
            "• १००% सोलर संचालित ग्रामपंचायत व स्ट्रीट लाइट्स",
            "• डिजिटल जि. प. प्राथमिक शाळा व मोफत वातानुकूलित अभ्यासिका",
            "• प्राथमिक आरोग्य उपकेंद्र व नियमित मोफत तपासणी",
            "• कचरा संकलन घंटागाडी व सेंद्रिय खत प्रकल्प"
          )

          amenities.forEach { am ->
            Text(text = am, fontSize = 11.sp, color = OnGreenContainer, lineHeight = 16.sp)
          }
        }
      }
    }
  }
}

@Composable
fun ProfileRow(label: String, value: String) {
  Row(
    modifier = Modifier.fillMaxWidth(),
    horizontalArrangement = Arrangement.SpaceBetween
  ) {
    Text(text = label, fontSize = 12.sp, color = TextSecondary)
    Text(text = value, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
  }
}

// 2. Schemes & Contacts
@Composable
fun SchemesAndContactsContent(
  schemes: List<GovScheme>,
  contacts: List<EmergencyContact>,
  lang: AppLanguage,
  onCallContact: (String) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    item {
      Text(
        text = if (lang == AppLanguage.MARATHI) "प्रमुख शासकीय योजना (Govt Schemes)" else "Government Schemes",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(schemes, key = { "scheme_${it.id}" }) { scheme ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
          Surface(
            shape = RoundedCornerShape(4.dp),
            color = GreenContainer
          ) {
            Text(
              text = scheme.category,
              fontSize = 10.sp,
              fontWeight = FontWeight.Bold,
              color = GreenPrimary,
              modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
            )
          }

          Text(
            text = scheme.titleMr,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = "लाभाचे स्वरूप: ${scheme.benefitsMr}",
            fontSize = 12.sp,
            color = GreenPrimary,
            fontWeight = FontWeight.SemiBold,
            lineHeight = 16.sp
          )

          Text(
            text = "पात्रता: ${scheme.eligibilityMr}",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
          )

          Text(
            text = "आवश्यक कागदपत्रे: ${scheme.requiredDocsMr}",
            fontSize = 11.sp,
            color = TextSecondary,
            lineHeight = 15.sp
          )
        }
      }
    }

    item {
      Text(
        text = if (lang == AppLanguage.MARATHI) "महत्त्वाचे अधिकारी व आपत्कालीन संपर्क" else "Directory & Contacts",
        fontSize = 15.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(top = 10.dp)
      )
    }

    items(contacts, key = { "contact_${it.id}" }) { contact ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = contact.designationMr,
              fontSize = 13.sp,
              fontWeight = FontWeight.Bold,
              color = if (contact.isEmergency) Color(0xFFC62828) else MaterialTheme.colorScheme.onSurface
            )
            Text(text = contact.personName, fontSize = 11.sp, color = TextSecondary)
            Text(text = contact.department, fontSize = 10.sp, color = GreenPrimary)
          }

          Button(
            onClick = { onCallContact(contact.phoneNumber) },
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
          ) {
            Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = contact.phoneNumber, fontSize = 11.sp)
          }
        }
      }
    }
  }
}

// 3. Admin Portal Content
@Composable
fun AdminPortalContent(
  uiState: GramSevaUiState,
  lang: AppLanguage,
  onUpdateComplaint: (ComplaintItem) -> Unit,
  onApproveCert: (String, String) -> Unit,
  onRejectCert: (String, String) -> Unit,
  onOpenNotice: () -> Unit,
  onUpdateWork: (String, Int, String) -> Unit
) {
  val pendingCerts = uiState.certificates.filter { it.status == "PENDING" }
  val activeComplaints = uiState.complaints.filter { it.status != "RESOLVED" && it.status != "CLOSED" }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Admin Dashboard Summary Cards
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SaffronContainer),
        border = androidx.compose.foundation.BorderStroke(1.5.dp, SaffronSecondary.copy(alpha = 0.5f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = SaffronSecondary)
              Text(
                text = "ग्रामपंचायत अधिकारी प्रशासन कक्ष",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = OnSaffronContainer
              )
            }
            Surface(
              shape = RoundedCornerShape(6.dp),
              color = SaffronSecondary
            ) {
              Text(
                text = "अधिकृत पोर्टल",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }
          }

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "प्रलंबित तक्रारी:", fontSize = 11.sp, color = OnSaffronContainer)
              Text(text = "${activeComplaints.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SaffronSecondary)
            }
            Column {
              Text(text = "प्रलंबित दाखले:", fontSize = 11.sp, color = OnSaffronContainer)
              Text(text = "${pendingCerts.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = SaffronSecondary)
            }
            Column {
              Text(text = "एकूण सूचना:", fontSize = 11.sp, color = OnSaffronContainer)
              Text(text = "${uiState.notices.size}", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            }
          }

          Button(
            onClick = onOpenNotice,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronSecondary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Campaign, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("नवीन जाहीर सूचना गावात प्रसिद्ध करा", fontSize = 12.sp, fontWeight = FontWeight.Bold)
          }
        }
      }
    }

    // Pending Certificates Approval Section
    item {
      Text(
        text = "दाखले अर्ज मंजुरी कक्ष (${pendingCerts.size} प्रलंबित)",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    if (pendingCerts.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = GreenContainer.copy(alpha = 0.4f))
        ) {
          Text(
            text = "सध्या कोणताही दाखला अर्ज मंजुरीसाठी प्रलंबित नाही.",
            fontSize = 12.sp,
            color = GreenPrimary,
            modifier = Modifier.padding(14.dp)
          )
        }
      }
    } else {
      items(pendingCerts, key = { "admin_cert_${it.id}" }) { cert ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
        ) {
          Column(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = cert.applicationId, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
              Text(text = cert.appliedDate, fontSize = 10.sp, color = TextSecondary)
            }

            Text(text = cert.certificateType, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            Text(text = "अर्जदार: ${cert.applicantName} | कारण: ${cert.purpose}", fontSize = 12.sp, color = TextSecondary)

            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              Button(
                onClick = { onApproveCert(cert.id, "अभिलेख तपासणी पूर्ण. दाखला मंजूर केला.") },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(8.dp)
              ) {
                Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("मंजूर करा", fontSize = 12.sp)
              }
              OutlinedButton(
                onClick = { onRejectCert(cert.id, "कागदपत्रे अपूर्ण असल्याने नामंजूर.") },
                modifier = Modifier.weight(0.7f),
                shape = RoundedCornerShape(8.dp)
              ) {
                Text("नामंजूर", fontSize = 12.sp, color = Color.Red)
              }
            }
          }
        }
      }
    }

    // Active Grievances to Update
    item {
      Text(
        text = "नागरिक तक्रार निवारण कृती कक्ष (${activeComplaints.size} सक्रिय)",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(activeComplaints, key = { "admin_comp_${it.id}" }) { comp ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(text = "${comp.complaintNumber} (${comp.category})", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            Text(text = "सद्य: ${comp.status}", fontSize = 10.sp, color = SaffronSecondary, fontWeight = FontWeight.Bold)
          }

          Text(text = comp.subject, fontSize = 13.sp, fontWeight = FontWeight.Bold)
          Text(text = "तक्रारदार: ${comp.citizenName} • प्रभाग ${comp.wardNumber}", fontSize = 11.sp, color = TextSecondary)

          Button(
            onClick = { onUpdateComplaint(comp) },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SaffronSecondary),
            shape = RoundedCornerShape(8.dp)
          ) {
            Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text("तक्रार स्थिती बदला व शेरा नोंदवा", fontSize = 12.sp)
          }
        }
      }
    }
  }
}
