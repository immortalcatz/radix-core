/*******************************************************************************
 * Language.java
 * Copyright (c) 2014 WildBamaBoy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the MCA Minecraft Mod license.
 ******************************************************************************/

package com.radixshock.radixcore.constant;

/**
 * List of language IDs recognized by Minecraft and their english names.
 */
public enum Language
{
	af_ZA("Afrikaans"), ar_SA("Arabic"), hy_AM("Armenian"), bg_BG("Bulgarian"), ca_ES("Catalan"), cs_CZ("Czech"), cy_GB("Welsh"), da_DK("Danish"), de_DE("German"), el_GR("Greek"), en_AU("English"), en_CA("English"), en_GB("English"), en_PT("Pirate"), en_US("English"), eo_UY("Esperanto"), es_AR("Argentina Spanish"), es_ES("Spanish"), es_MX("Mexico Spanish"), es_UY("Uruguay Spanish"), es_VE("Venezuela Spanish"), et_EE("Estonian"), eu_ES("Basque"), fi_FI("Finnish"), fr_FR("French"), fr_CA(
			"Canadian French"), ga_IE("Irish"), gl_ES("Galician"), he_IL("Hebrew"), hi_IN("Hindi"), hr_HR("Croatian"), hu_HU("Hungarian"), id_ID("Bahasa Indonesia"), is_IS("Icelandic"), it_IT("Italian"), ja_JP("Japanese"), ka_GE("Georgian"), ko_KR("Korean"), ko_KO("Cornish"), la_LA("Latin"), lb_LB("Luxembourgish"), lt_LT("Lithuanian"), lv_LV("Latvian"), ms_MY("Malay"), mt_MT("Maltese"), nl_NL("Dutch"), nn_NO("Nynorsk"), nb_NO("Norwegian"), oc_OC("Occitan"), pl_PL("Polish"), pt_BR(
			"Brazilian Portuguese"), pt_PT("Portuguese"), qya_AA("Quenya"), ro_RO("Romanian"), ru_RU("Russian"), sk_SK("Slovak"), sl_SI("Slovenian"), sr_SP("Serbian"), sv_SE("Swedish"), th_TH("Thai"), tlh_AA("Klingon"), tr_TR("Turkish"), uk_UA("Ukrainian"), vi_VN("Vietnamese"), zh_CN("Chinese Simplified"), zh_TW("Chinese Traditional");

	private String englishName;

	private Language(String englishName)
	{
		this.englishName = englishName;
	}

	public String getEnglishName()
	{
		return englishName;
	}
}
