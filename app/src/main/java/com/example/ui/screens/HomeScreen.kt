package com.example.ui.screens

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.NoticeItem
import com.example.ui.components.GramPanchayatEmblem
import com.example.ui.localization.AppLanguage
import com.example.ui.localization.GramSevaStrings
import com.example.ui.theme.*
import com.example.ui.viewmodel.GramSevaUiState

@Composable
fun HomeScreen(
  uiState: GramSevaUiState,
  onNavigateToTab: (Int) -> Unit,
  onOpenComplaintDialog: () -> Unit,
  onSelectNotice: (NoticeItem) -> Unit,
  modifier: Modifier = Modifier
) {
  val context = LocalContext.current
  val lang = uiState.language
  val citizen = uiState.citizen

  LazyColumn(
    modifier = modifier.fillMaxSize(),
    contentPadding = PaddingValues(bottom = 90.dp),
    verticalArrangement = Arrangement.spacedBy(16.dp)
  ) {
    // 1. Hero Banner with Image & Civic Identity
    item {
      Box(
        modifier = Modifier
          .fillMaxWidth()
          .height(210.dp)
      ) {
        Image(
          painter = painterResource(id = R.drawable.img_gram_hero_1790872562328),
          contentDescription = "Gram Panchayat Model Village",
          modifier = Modifier.fillMaxSize(),
          contentScale = ContentScale.Crop
        )

        // Gradient overlay for contrast
        Box(
          modifier = Modifier
            .fillMaxSize()
            .background(
              Brush.verticalGradient(
                colors = listOf(
                  Color.Black.copy(alpha = 0.35f),
                  Color.Black.copy(alpha = 0.85f)
                )
              )
            )
        )

        // Content on Hero
        Column(
          modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
          verticalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Surface(
              shape = RoundedCornerShape(20.dp),
              color = GreenPrimary.copy(alpha = 0.85f)
            ) {
              Row(
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
              ) {
                Icon(Icons.Default.WorkspacePremium, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(16.dp))
                Text(
                  text = if (lang == AppLanguage.MARATHI) "आदर्श स्मार्ट ग्रामपंचायत" else "Model Smart Village",
                  color = Color.White,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(12.dp),
              color = Color.Black.copy(alpha = 0.5f)
            ) {
              Text(
                text = "GP CODE: 2715004",
                color = Color.White.copy(alpha = 0.9f),
                fontSize = 10.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
              )
            }
          }

          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              GramPanchayatEmblem(size = 36)
              Column {
                Text(
                  text = GramSevaStrings.villageName(lang),
                  color = Color.White,
                  fontSize = 20.sp,
                  fontWeight = FontWeight.ExtraBold
                )
                Text(
                  text = GramSevaStrings.villageSubtitle(lang),
                  color = Color.White.copy(alpha = 0.85f),
                  fontSize = 12.sp
                )
              }
            }
          }
        }
      }
    }

    // 2. Citizen Welcome & Property Tag
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalAlignment = Alignment.CenterVertically,
          horizontalArrangement = Arrangement.SpaceBetween
        ) {
          Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(GreenContainer),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.Person, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(26.dp))
            }
            Column {
              Text(
                text = if (lang == AppLanguage.MARATHI) "नमस्ते, ${citizen?.fullName ?: "नागरिक"}" else "Namaste, ${citizen?.fullName ?: "Citizen"}",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
              )
              Text(
                text = "प्रभाग क्र. ${citizen?.wardNumber ?: 2} | घर नं. १२४ (निचपूर)",
                fontSize = 11.sp,
                color = TextSecondary
              )
            }
          }

          Surface(
            shape = RoundedCornerShape(8.dp),
            color = if (uiState.isAdminMode) SaffronContainer else GreenContainer
          ) {
            Text(
              text = if (uiState.isAdminMode) "अधिकारी" else "नागरिक",
              color = if (uiState.isAdminMode) OnSaffronContainer else GreenPrimary,
              fontSize = 11.sp,
              fontWeight = FontWeight.Bold,
              modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
            )
          }
        }
      }
    }

    // 2.5 Featured Officer Desk - श्री. महेश मुंडे
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = GreenContainer.copy(alpha = 0.55f)),
        border = androidx.compose.foundation.BorderStroke(1.dp, GreenPrimary.copy(alpha = 0.35f))
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
          verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
          ) {
            Row(
              verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
              Surface(
                shape = CircleShape,
                color = GreenPrimary
              ) {
                Box(
                  modifier = Modifier.size(40.dp),
                  contentAlignment = Alignment.Center
                ) {
                  Icon(
                    Icons.Default.AccountBalance,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(22.dp)
                  )
                }
              }
              Column {
                Text(
                  text = "ग्रामसेवक / ग्रामविकास अधिकारी कक्ष",
                  fontSize = 11.sp,
                  fontWeight = FontWeight.SemiBold,
                  color = GreenPrimary
                )
                Text(
                  text = "श्री. महेश मुंडे",
                  fontSize = 17.sp,
                  fontWeight = FontWeight.ExtraBold,
                  color = MaterialTheme.colorScheme.onSurface
                )
              }
            }

            Surface(
              shape = RoundedCornerShape(6.dp),
              color = GreenPrimary
            ) {
              Text(
                text = "प्रशासकीय प्रमुख",
                color = Color.White,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
              )
            }
          }

          Text(
            text = "ग्रामपंचायत निचपूर (ता. किनवट, जि. नांदेड) • शासकीय दाखले, कर आकारणी व लोककल्याणकारी योजनांचे अधिकृत नोडल अधिकारी.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            lineHeight = 15.sp
          )

          Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
          ) {
            Button(
              onClick = {
                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9850123456"))
                context.startActivity(intent)
              },
              modifier = Modifier.weight(1f),
              colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary),
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(vertical = 6.dp, horizontal = 10.dp)
            ) {
              Icon(Icons.Default.Phone, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("थेट संपर्क (९८५०१२३४५६)", fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
              onClick = { onNavigateToTab(1) },
              shape = RoundedCornerShape(8.dp),
              contentPadding = PaddingValues(vertical = 6.dp, horizontal = 10.dp)
            ) {
              Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(14.dp))
              Spacer(modifier = Modifier.width(4.dp))
              Text("दाखला सेवा", fontSize = 11.sp)
            }
          }
        }
      }
    }

    // 3. Urgent Ticker / Top Notice
    if (uiState.notices.isNotEmpty()) {
      val topNotice = uiState.notices.firstOrNull { it.priority == "HIGH" } ?: uiState.notices.first()
      item {
        Card(
          modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clickable { onNavigateToTab(3) },
          shape = RoundedCornerShape(14.dp),
          colors = CardDefaults.cardColors(containerColor = SaffronContainer.copy(alpha = 0.7f)),
          border = androidx.compose.foundation.BorderStroke(1.dp, SaffronSecondary.copy(alpha = 0.4f))
        ) {
          Row(
            modifier = Modifier
              .fillMaxWidth()
              .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
          ) {
            Box(
              modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SaffronSecondary),
              contentAlignment = Alignment.Center
            ) {
              Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            }

            Column(modifier = Modifier.weight(1f)) {
              Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
              ) {
                Surface(
                  shape = RoundedCornerShape(4.dp),
                  color = Color.Red
                ) {
                  Text(
                    text = "महत्त्वाचे",
                    color = Color.White,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                  )
                }
                Text(
                  text = topNotice.category,
                  fontSize = 11.sp,
                  fontWeight = FontWeight.Bold,
                  color = SaffronSecondary
                )
              }
              Text(
                text = topNotice.titleMr,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = OnSaffronContainer,
                maxLines = 1
              )
              Text(
                text = topNotice.descriptionMr,
                fontSize = 11.sp,
                color = OnSaffronContainer.copy(alpha = 0.85f),
                maxLines = 1
              )
            }

            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = SaffronSecondary)
          }
        }
      }
    }

    // 4. Quick Services 6-Card Grid
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
      ) {
        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.SpaceBetween,
          verticalAlignment = Alignment.CenterVertically
        ) {
          Text(
            text = GramSevaStrings.quickServicesTitle(lang),
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
          )
          Text(
            text = "६ ऑनलाइन सेवा",
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = GreenPrimary
          )
        }

        val services = listOf(
          Triple(GramSevaStrings.btnFileComplaint(lang), Icons.Default.ReportProblem, GreenPrimary),
          Triple(GramSevaStrings.btnPayTax(lang), Icons.Default.ReceiptLong, SaffronSecondary),
          Triple(GramSevaStrings.btnCertificates(lang), Icons.Default.Description, NavyTertiary),
          Triple(GramSevaStrings.btnDevelopmentWorks(lang), Icons.Default.Engineering, Color(0xFF5D4037)),
          Triple(GramSevaStrings.btnGramSabha(lang), Icons.Default.Groups, Color(0xFF00796B)),
          Triple(GramSevaStrings.btnSchemes(lang), Icons.Default.Stars, Color(0xFFC2185B))
        )

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
          services.chunked(3).forEachIndexed { rowIndex, rowList ->
            Row(
              modifier = Modifier.fillMaxWidth(),
              horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
              rowList.forEachIndexed { colIndex, (title, icon, color) ->
                val flatIndex = rowIndex * 3 + colIndex
                Card(
                  modifier = Modifier
                    .weight(1f)
                    .height(96.dp)
                    .clickable {
                      when (flatIndex) {
                        0 -> onOpenComplaintDialog()
                        1 -> onNavigateToTab(1) // Services (Bills tab)
                        2 -> onNavigateToTab(1) // Services (Certificates tab)
                        3 -> onNavigateToTab(1) // Services (Works tab)
                        4 -> onNavigateToTab(3) // Sabha & Notices
                        5 -> onNavigateToTab(4) // Schemes in more
                      }
                    },
                  shape = RoundedCornerShape(14.dp),
                  colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                  elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                  Column(
                    modifier = Modifier
                      .fillMaxSize()
                      .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                  ) {
                    Box(
                      modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .background(color.copy(alpha = 0.12f)),
                      contentAlignment = Alignment.Center
                    ) {
                      Icon(imageVector = icon, contentDescription = title, tint = color, modifier = Modifier.size(20.dp))
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                      text = title,
                      fontSize = 11.sp,
                      fontWeight = FontWeight.Bold,
                      color = MaterialTheme.colorScheme.onSurface,
                      textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                      maxLines = 2,
                      lineHeight = 14.sp
                    )
                  }
                }
              }
            }
          }
        }
      }
    }

    // 5. Village Stats Metric Row
    item {
      Surface(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
      ) {
        Row(
          modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp, horizontal = 8.dp),
          horizontalArrangement = Arrangement.SpaceEvenly,
          verticalAlignment = Alignment.CenterVertically
        ) {
          StatItem(number = "४,८५०", label = "एकूण लोकसंख्या", icon = Icons.Default.People)
          Box(modifier = Modifier.width(1.dp).height(30.dp).background(OutlineLight))
          StatItem(number = "६", label = "प्रभाग रचना", icon = Icons.Default.LocationCity)
          Box(modifier = Modifier.width(1.dp).height(30.dp).background(OutlineLight))
          StatItem(number = "९८%", label = "तक्रार निवारण", icon = Icons.Default.TaskAlt)
          Box(modifier = Modifier.width(1.dp).height(30.dp).background(OutlineLight))
          StatItem(number = "१००%", label = "हर घर जल", icon = Icons.Default.Water)
        }
      }
    }

    // 6. Key Officials Card
    item {
      Card(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
      ) {
        Column(
          modifier = Modifier
            .fillMaxWidth()
            .padding(14.dp),
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
              Icon(Icons.Default.Badge, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(20.dp))
              Text(
                text = GramSevaStrings.keyOfficials(lang),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
              )
            }
            Text(
              text = "कार्यकाळ २०२३-२८",
              fontSize = 10.sp,
              color = TextSecondary
            )
          }

          HorizontalDivider()

          val officials = listOf(
            Triple("मा. श्री. सदाशिव मुंडे", "सरपंच", "9422011234"),
            Triple("श्री. पंकज पाटील", "उपसरपंच", "9823456789"),
            Triple("श्री. महेश मुंडे", "ग्रामसेवक / ग्रा.वि.अ.", "9850123456"),
            Triple("श्री. अशोक घारगे", "तलाठी (महसूल)", "9403124567")
          )

          Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            officials.forEach { (name, post, phone) ->
              Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
              ) {
                Column(modifier = Modifier.weight(1f)) {
                  Text(text = name, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                  Text(text = post, fontSize = 11.sp, color = GreenPrimary, fontWeight = FontWeight.SemiBold)
                }
                IconButton(
                  onClick = {
                    val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phone"))
                    context.startActivity(intent)
                  },
                  modifier = Modifier.size(36.dp)
                ) {
                  Icon(Icons.Default.Phone, contentDescription = "Call $name", tint = GreenPrimary, modifier = Modifier.size(18.dp))
                }
              }
            }
          }
        }
      }
    }

    // 7. 24x7 Emergency Helplines Quick Access
    item {
      Column(
        modifier = Modifier
          .fillMaxWidth()
          .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
      ) {
        Text(
          text = GramSevaStrings.emergencyContactsTitle(lang),
          fontSize = 15.sp,
          fontWeight = FontWeight.Bold,
          color = MaterialTheme.colorScheme.onSurface
        )

        Row(
          modifier = Modifier.fillMaxWidth(),
          horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
          EmergencyChip(
            title = "रुग्णवाहिका",
            number = "108",
            icon = Icons.Default.MedicalServices,
            color = Color(0xFFC62828),
            modifier = Modifier.weight(1f),
            onCall = {
              context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:108")))
            }
          )
          EmergencyChip(
            title = "आरोग्य केंद्र",
            number = "02469-222100",
            icon = Icons.Default.LocalHospital,
            color = Color(0xFF00695C),
            modifier = Modifier.weight(1f),
            onCall = {
              context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:02469222100")))
            }
          )
          EmergencyChip(
            title = "वीज तक्रार",
            number = "1912",
            icon = Icons.Default.ElectricBolt,
            color = Color(0xFFEF6C00),
            modifier = Modifier.weight(1f),
            onCall = {
              context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:1912")))
            }
          )
          EmergencyChip(
            title = "पोलीस मदत",
            number = "112",
            icon = Icons.Default.LocalPolice,
            color = Color(0xFF283593),
            modifier = Modifier.weight(1f),
            onCall = {
              context.startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:112")))
            }
          )
        }
      }
    }
  }
}

@Composable
fun StatItem(number: String, label: String, icon: ImageVector) {
  Column(horizontalAlignment = Alignment.CenterHorizontally) {
    Icon(icon, contentDescription = null, tint = GreenPrimary, modifier = Modifier.size(16.dp))
    Text(text = number, fontSize = 14.sp, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onSurface)
    Text(text = label, fontSize = 9.sp, color = TextSecondary)
  }
}

@Composable
fun EmergencyChip(
  title: String,
  number: String,
  icon: ImageVector,
  color: Color,
  modifier: Modifier = Modifier,
  onCall: () -> Unit
) {
  Surface(
    shape = RoundedCornerShape(12.dp),
    color = color.copy(alpha = 0.1f),
    border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
    modifier = modifier.clickable { onCall() }
  ) {
    Column(
      modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
      horizontalAlignment = Alignment.CenterHorizontally
    ) {
      Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
      Spacer(modifier = Modifier.height(2.dp))
      Text(text = title, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = color)
      Text(text = number, fontSize = 9.sp, color = TextSecondary)
    }
  }
}
