package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.BillItem
import com.example.data.model.CertificateApplication
import com.example.data.model.DevelopmentWork
import com.example.ui.components.CertificateStatusChip
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.GramSevaStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.GramSevaUiState

@Composable
fun ServicesScreen(
  uiState: GramSevaUiState,
  onSubTabChange: (Int) -> Unit,
  onOpenNewCertificate: () -> Unit,
  onSelectCertificate: (CertificateApplication) -> Unit,
  onSelectBillToPay: (BillItem) -> Unit,
  onSelectBillReceipt: (BillItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val lang = uiState.language
  val currentSubTab = uiState.servicesSubTab

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
          text = if (lang == AppLanguage.MARATHI) "नागरिक सेवा व कर भरणा" else "Services & Tax Center",
          fontSize = 20.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )
        Text(
          text = "दाखले, कर भरणा व विकासकामे डिजिटल पोर्टल",
          fontSize = 11.sp,
          color = TextSecondary
        )
      }
    }

    // 3 Sub-Tabs: Certificates, Tax/Bills, Development Works
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
          title = GramSevaStrings.certificatesTab(lang),
          icon = Icons.Default.Description,
          isSelected = currentSubTab == 0,
          onClick = { onSubTabChange(0) },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = GramSevaStrings.billsTab(lang),
          icon = Icons.Default.ReceiptLong,
          isSelected = currentSubTab == 1,
          onClick = { onSubTabChange(1) },
          modifier = Modifier.weight(1f)
        )
        SubTabPill(
          title = GramSevaStrings.developmentTab(lang),
          icon = Icons.Default.Engineering,
          isSelected = currentSubTab == 2,
          onClick = { onSubTabChange(2) },
          modifier = Modifier.weight(1f)
        )
      }
    }

    // Sub-Tab Content
    when (currentSubTab) {
      0 -> CertificatesContent(
        certificates = uiState.certificates,
        lang = lang,
        onApplyClick = onOpenNewCertificate,
        onViewCertClick = onSelectCertificate
      )
      1 -> BillsContent(
        bills = uiState.bills,
        lang = lang,
        onPayClick = onSelectBillToPay,
        onReceiptClick = onSelectBillReceipt
      )
      2 -> DevelopmentWorksContent(
        works = uiState.developmentWorks,
        lang = lang
      )
    }
  }
}

@Composable
fun SubTabPill(
  title: String,
  icon: androidx.compose.ui.graphics.vector.ImageVector,
  isSelected: Boolean,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .clip(RoundedCornerShape(10.dp))
      .background(if (isSelected) MaterialTheme.colorScheme.surface else Color.Transparent)
      .clickable { onClick() }
      .padding(vertical = 8.dp),
    contentAlignment = Alignment.Center
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
      Icon(
        imageVector = icon,
        contentDescription = null,
        tint = if (isSelected) GreenPrimary else TextSecondary,
        modifier = Modifier.size(16.dp)
      )
      Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
        color = if (isSelected) GreenPrimary else TextSecondary
      )
    }
  }
}

// 1. Certificates Tab
@Composable
fun CertificatesContent(
  certificates: List<CertificateApplication>,
  lang: AppLanguage,
  onApplyClick: () -> Unit,
  onViewCertClick: (CertificateApplication) -> Unit
) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(12.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Apply CTA Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = GreenContainer)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(modifier = Modifier.weight(1f)) {
            Text(
              text = if (lang == AppLanguage.MARATHI) "डिजिटल दाखला सेवा" else "Digital Certificates",
              fontSize = 15.sp,
              fontWeight = FontWeight.Bold,
              color = OnGreenContainer
            )
            Text(
              text = "रहिवासी, जन्म, मृत्यू व ना-हरकत (NOC) दाखल्यासाठी घरबसल्या अर्ज करा.",
              fontSize = 11.sp,
              color = TextSecondary,
              lineHeight = 15.sp
            )
          }
          Button(
            onClick = onApplyClick,
            colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
            shape = RoundedCornerShape(10.dp)
          ) {
            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (lang == AppLanguage.MARATHI) "अर्ज करा" else "Apply", fontSize = 12.sp)
          }
        }
      }
    }

    item {
      Text(
        text = if (lang == AppLanguage.MARATHI) "माझे सादर केलेले अर्ज" else "My Submitted Applications",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(certificates, key = { "cert_${it.id}" }) { cert ->
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
            Text(
              text = cert.applicationId,
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = GreenPrimary
            )
            CertificateStatusChip(status = cert.status, language = lang)
          }

          Text(
            text = cert.certificateType,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = "अर्जदार: ${cert.applicantName} | कारण: ${cert.purpose}",
            fontSize = 12.sp,
            color = TextSecondary
          )

          if (cert.remarks.isNotEmpty()) {
            Surface(
              shape = RoundedCornerShape(8.dp),
              color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
              modifier = Modifier.fillMaxWidth()
            ) {
              Text(
                text = "शेरा: ${cert.remarks}",
                fontSize = 11.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(8.dp)
              )
            }
          }

          HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Text(
              text = "अर्ज दिनांक: ${cert.appliedDate}",
              fontSize = 11.sp,
              color = TextSecondary
            )

            if (cert.status == "APPROVED") {
              Button(
                onClick = { onViewCertClick(cert) },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
              ) {
                Icon(Icons.Default.DownloadDone, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(if (lang == AppLanguage.MARATHI) "दाखला पहा" else "View Certificate", fontSize = 12.sp)
              }
            } else {
              OutlinedButton(
                onClick = { /* Info */ },
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
              ) {
                Text(if (lang == AppLanguage.MARATHI) "पडताळणी सुरू" else "Under Process", fontSize = 11.sp)
              }
            }
          }
        }
      }
    }
  }
}

// 2. Bills & Taxes Tab
@Composable
fun BillsContent(
  bills: List<BillItem>,
  lang: AppLanguage,
  onPayClick: (BillItem) -> Unit,
  onReceiptClick: (BillItem) -> Unit
) {
  val pendingBills = bills.filter { !it.isPaid }
  val paidBills = bills.filter { it.isPaid }
  val totalPending = pendingBills.sumOf { it.amount }

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    verticalArrangement = Arrangement.spacedBy(14.dp),
    contentPadding = PaddingValues(bottom = 90.dp)
  ) {
    // Total Dues Banner
    item {
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SaffronContainer),
        border = androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondary.copy(alpha = 0.3f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
          verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column {
              Text(
                text = if (lang == AppLanguage.MARATHI) "एकूण थकीत कर देयके" else "Total Outstanding Dues",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = OnSaffronContainer
              )
              Text(
                text = "₹ ${totalPending.toInt()}/-",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = SaffronSecondary
              )
            }
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = Color.Red
            ) {
              Text(
                text = "१५% सवलत ३१ ऑक्टो. पर्यंत",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }
          Text(
            text = "मालमत्ता क्र.: GP/NCH/2026/0124 • प्रभाग क्र. २ (निचपूर)",
            fontSize = 11.sp,
            color = OnSaffronContainer.copy(alpha = 0.8f)
          )
        }
      }
    }

    // Pending Bills List
    item {
      Text(
        text = if (lang == AppLanguage.MARATHI) "थकीत बिले (Pending Bills)" else "Pending Dues",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    if (pendingBills.isEmpty()) {
      item {
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(12.dp),
          colors = CardDefaults.cardColors(containerColor = GreenContainer)
        ) {
          Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = GreenPrimary)
            Text(
              text = if (lang == AppLanguage.MARATHI) "अभिनंदन! आपले सर्व ग्रामपंचायत कर पूर्ण भरलेले आहेत." else "All taxes are paid up to date!",
              fontSize = 12.sp,
              fontWeight = FontWeight.Bold,
              color = OnGreenContainer
            )
          }
        }
      }
    } else {
      items(pendingBills, key = { "pending_bill_${it.id}" }) { bill ->
        Card(
          modifier = Modifier.fillMaxWidth(),
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
          elevation = CardDefaults.cardElevation(defaultElevation = 1.5.dp)
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              Text(
                text = bill.billType,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "आकारणी वर्ष: ${bill.assessmentYear}",
                fontSize = 11.sp,
                color = TextSecondary
              )
              Text(
                text = "अंतिम मुदत: ${bill.dueDate}",
                fontSize = 11.sp,
                color = Color(0xFFC62828),
                fontWeight = FontWeight.SemiBold
              )
            }

            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(6.dp)) {
              Text(
                text = "₹ ${bill.amount.toInt()}/-",
                fontSize = 17.sp,
                fontWeight = FontWeight.ExtraBold,
                color = GreenPrimary
              )
              Button(
                onClick = { onPayClick(bill) },
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 6.dp)
              ) {
                Icon(Icons.Default.Payment, contentDescription = null, modifier = Modifier.size(14.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(if (lang == AppLanguage.MARATHI) "आता भरा" else "Pay Now", fontSize = 12.sp)
              }
            }
          }
        }
      }
    }

    // Paid History
    item {
      Text(
        text = if (lang == AppLanguage.MARATHI) "मागील भरणा पावती इतिहास (Paid Receipts)" else "Payment History",
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface
      )
    }

    items(paidBills, key = { "paid_bill_${it.id}" }) { bill ->
      Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
              Icon(Icons.Default.Verified, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
              Text(
                text = bill.billType,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "पावती क्र.: ${bill.receiptNumber} • ${bill.paidDate}",
              fontSize = 10.sp,
              color = TextSecondary
            )
          }

          Button(
            onClick = { onReceiptClick(bill) },
            colors = ButtonDefaults.buttonColors(containerColor = GreenContainer),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
          ) {
            Icon(Icons.Default.Receipt, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(4.dp))
            Text(if (lang == AppLanguage.MARATHI) "पावती पहा" else "Receipt", fontSize = 11.sp, color = GreenPrimary)
          }
        }
      }
    }
  }
}

// 3. Development Projects Tab
@Composable
fun DevelopmentWorksContent(
  works: List<DevelopmentWork>,
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
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
      ) {
        Column(
          modifier = Modifier.padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
          Text(
            text = if (lang == AppLanguage.MARATHI) "गावातील प्रमुख विकास प्रकल्प (२०२६-२७)" else "Village Development Projects",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "पारदर्शक विकासकामे: मंजूर निधी, कामाची प्रगती व प्रत्यक्ष स्थिती",
            fontSize = 11.sp,
            color = TextSecondary
          )
        }
      }
    }

    items(works, key = { "work_${it.id}" }) { work ->
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
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(4.dp),
              color = GreenContainer
            ) {
              Text(
                text = work.category,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = GreenPrimary,
                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
              )
            }

            Surface(
              shape = RoundedCornerShape(10.dp),
              color = if (work.status == "COMPLETED") GreenContainer else SaffronContainer
            ) {
              Text(
                text = if (work.status == "COMPLETED") "काम पूर्ण झाले (100%)" else "प्रगतीपथावर (${work.progressPercent}%)",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (work.status == "COMPLETED") GreenPrimary else SaffronSecondary,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Text(
            text = work.titleMr,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )

          Text(
            text = work.workDetailsMr,
            fontSize = 12.sp,
            color = TextSecondary,
            lineHeight = 17.sp
          )

          // Progress Bar
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.SpaceBetween
            ) {
              Text(text = "कामाची प्रगती:", fontSize = 11.sp, color = TextSecondary)
              Text(text = "${work.progressPercent}%", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            }
            LinearProgressIndicator(
              progress = { work.progressPercent / 100f },
              modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
              color = if (work.progressPercent == 100) GreenPrimary else SaffronSecondary,
              trackColor = OutlineLight.copy(alpha = 0.4f)
            )
          }

          HorizontalDivider(color = OutlineLight.copy(alpha = 0.5f))

          // Budget & Contractor
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Column {
              Text(text = "मंजूर निधी:", fontSize = 10.sp, color = TextSecondary)
              Text(text = work.budgetAmount, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = GreenPrimary)
            }
            Column(horizontalAlignment = Alignment.End) {
              Text(text = "कार्यकारी यंत्रणा:", fontSize = 10.sp, color = TextSecondary)
              Text(text = work.contractorAgency, fontSize = 11.sp, fontWeight = FontWeight.SemiBold, maxLines = 1)
            }
          }
        }
      }
    }
  }
}
