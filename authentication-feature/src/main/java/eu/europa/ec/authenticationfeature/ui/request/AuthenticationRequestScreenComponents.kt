/*
 *
 *  * Copyright (c) 2023 European Commission
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package eu.europa.ec.authenticationfeature.ui.request

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import eu.europa.ec.authenticationfeature.ui.request.model.AuthenticationRequestDataUi
import eu.europa.ec.authenticationfeature.ui.request.model.OptionalFieldItemUi
import eu.europa.ec.authenticationfeature.ui.request.model.RequiredFieldsItemUi
import eu.europa.ec.commonfeature.utils.PreviewTheme
import eu.europa.ec.resourceslogic.theme.values.primaryDark
import eu.europa.ec.resourceslogic.theme.values.textPrimaryDark
import eu.europa.ec.resourceslogic.theme.values.warning
import eu.europa.ec.uilogic.component.AppIcons
import eu.europa.ec.uilogic.component.CardWithIconAndText
import eu.europa.ec.uilogic.component.CheckboxWithContent
import eu.europa.ec.uilogic.component.InfoTextWithNameAndValue
import eu.europa.ec.uilogic.component.InfoTextWithNameAndValueData
import eu.europa.ec.uilogic.component.utils.SPACING_EXTRA_SMALL
import eu.europa.ec.uilogic.component.utils.SPACING_MEDIUM
import eu.europa.ec.uilogic.component.utils.VSpacer
import eu.europa.ec.uilogic.component.wrap.CheckboxData
import eu.europa.ec.uilogic.component.wrap.WrapExpandableCard
import eu.europa.ec.uilogic.component.wrap.WrapIcon

@Composable
fun <T> AuthenticationRequest(
    modifier: Modifier = Modifier,
    items: List<AuthenticationRequestDataUi<T>>,
    isShowingFullUserInfo: Boolean,
    onEventSend: (T) -> Unit,
    listState: LazyListState,
    contentPadding: PaddingValues = PaddingValues(all = 0.dp),
) {
    LazyColumn(
        modifier = modifier,
        state = listState,
        verticalArrangement = Arrangement.Center
    ) {

        itemsIndexed(items) { index, item ->
            when (item) {
                is AuthenticationRequestDataUi.Divider -> {
                    Divider()
                }

                is AuthenticationRequestDataUi.Identification -> {
                    IdentificationPartyCard(
                        cardText = item.identificationItemUi.title,
                        paddingValues = contentPadding,
                    )
                }

                is AuthenticationRequestDataUi.OptionalField -> {
                    OptionalField(
                        item = item.optionalFieldItemUi,
                        showFullDetails = isShowingFullUserInfo,
                        onEventSend = onEventSend,
                    )
                }

                is AuthenticationRequestDataUi.RequiredFields -> {
                    RequiredFields(
                        item = item.requiredFieldsItemUi,
                        onEventSend = onEventSend,
                        contentPadding = contentPadding,
                    )
                }

                is AuthenticationRequestDataUi.Space -> {
                    VSpacer.Custom(space = item.space)
                }
            }
        }
    }
}

@Composable
fun IdentificationPartyCard(
    cardText: String,
    paddingValues: PaddingValues,
) {
    CardWithIconAndText(
        modifier = Modifier.padding(
            start = paddingValues.calculateStartPadding(LayoutDirection.Ltr)
        ),
        text = {
            Text(
                text = cardText,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primaryDark
            )
        },
        icon = {
            WrapIcon(
                modifier = Modifier.size(50.dp),
                iconData = AppIcons.Id,
                customTint = MaterialTheme.colorScheme.primary
            )
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryDark.copy(alpha = 0.12f),
        ),
        contentPadding = PaddingValues(horizontal = SPACING_MEDIUM.dp)
    )
}

@Composable
fun <T> OptionalField(
    item: OptionalFieldItemUi<T>,
    showFullDetails: Boolean,
    onEventSend: (T) -> Unit,
) {
    CheckboxWithContent(
        modifier = Modifier.fillMaxWidth(),
        checkboxData = CheckboxData(
            isChecked = item.userIdentificationUi.checked,
            enabled = item.userIdentificationUi.enabled,
            onCheckedChange = {
                item.userIdentificationUi.event?.let { event ->
                    onEventSend(event)
                }
            }
        )
    ) {
        val infoValueStyle = if (item.userIdentificationUi.checked) {
            MaterialTheme.typography.titleMedium
        } else {
            MaterialTheme.typography.bodyLarge
        }
        if (showFullDetails) {
            InfoTextWithNameAndValue(
                itemData = InfoTextWithNameAndValueData(
                    infoName = item.userIdentificationUi.userIdentificationDomain.name,
                    infoValue = item.userIdentificationUi.userIdentificationDomain.value,
                ),
                infoValueTextStyle = infoValueStyle
            )
        } else {
            Text(
                text = item.userIdentificationUi.userIdentificationDomain.name,
                style = infoValueStyle
            )
        }
    }
}

@Composable
fun <T> RequiredFields(
    item: RequiredFieldsItemUi<T>,
    onEventSend: (T) -> Unit,
    contentPadding: PaddingValues,
) {
    val requiredFieldsTextStyle = MaterialTheme.typography.titleMedium
    val requiredFieldsTitlePadding = PaddingValues(
        horizontal = contentPadding.calculateStartPadding(LayoutDirection.Ltr) + SPACING_EXTRA_SMALL.dp,
        vertical = contentPadding.calculateBottomPadding()
    )

    WrapExpandableCard(
        modifier = Modifier.fillMaxWidth(),
        cardTitleContent = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = item.title,
                    style = requiredFieldsTextStyle
                )
                val icon = if (item.expanded) {
                    AppIcons.KeyboardArrowUp
                } else {
                    AppIcons.KeyboardArrowDown
                }
                WrapIcon(
                    iconData = icon,
                    customTint = MaterialTheme.colorScheme.primary
                )
            }
        },
        cardTitlePadding = requiredFieldsTitlePadding,
        cardContent = {
            item.userIdentificationsUi.forEach { requiredUserIdentificationUi ->
                val checkboxData = CheckboxData(
                    isChecked = requiredUserIdentificationUi.checked,
                    enabled = requiredUserIdentificationUi.enabled,
                    onCheckedChange = null
                )
                CheckboxWithContent(
                    modifier = Modifier.fillMaxWidth(),
                    checkboxData = checkboxData
                ) {
                    Text(
                        text = requiredUserIdentificationUi.userIdentificationDomain.name,
                        style = requiredFieldsTextStyle
                    )
                }
            }
        },
        cardContentPadding = PaddingValues(all = SPACING_EXTRA_SMALL.dp),
        onCardClick = { onEventSend(item.event) },
        expandCard = item.expanded,
    )
}

@Composable
fun WarningCard(warningText: String) {
    CardWithIconAndText(
        text = {
            Text(
                text = warningText,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.textPrimaryDark
            )
        },
        icon = {
            WrapIcon(
                modifier = Modifier.size(32.dp),
                iconData = AppIcons.Warning,
                customTint = MaterialTheme.colorScheme.warning
            )
        },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.warning.copy(alpha = 0.12f)
        ),
        contentPadding = PaddingValues(SPACING_MEDIUM.dp)
    )
}

@Preview(showBackground = true)
@Composable
private fun IdentificationPartyCardPreview() {
    PreviewTheme {
        IdentificationPartyCard(
            cardText = "Warning",
            paddingValues = PaddingValues(SPACING_MEDIUM.dp)
        )
    }
}