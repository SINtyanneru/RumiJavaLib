package com.rumisystem.rumi_java_lib;

public class OGP {
	private String SITE_NAME;
	private String TITLE;
	private String DESC;
	private String IMAGE;
	private SiteType TYPE;

	public enum SiteType{
		WebSite,
		Article
	}

	public void SetSIteNAME(String TEXT) {
		SITE_NAME = TEXT;
	}

	public void SetTITLE(String TEXT) {
		TITLE = TEXT;
	}

	public void SetDESC(String TEXT) {
		DESC = TEXT;
	}

	public void SetIMAGE_URL(String TEXT) {
		IMAGE = TEXT;
	}

	public void SetTYPE(SiteType TYPE) {
		this.TYPE = TYPE;
	}

	public String Build() {
		StringBuilder SB = new StringBuilder();

		//サイト名
		SB.append("<META PROPERTY=\"og:site_name\" CONTENT=\"" + SITE_NAME + "\" />");

		//タイトル
		SB.append("<META PROPERTY=\"og:title\" CONTENT=\"" + TITLE + "\" />");

		//説明名
		SB.append("<META PROPERTY=\"og:description\" CONTENT=\"" + DESC + "\" />");
		SB.append("<META NAME=\"description\" CONTENT=\"" + DESC + "\" />");

		//画像
		if (IMAGE != null) {
			SB.append("<META PROPERTY=\"og:image\" CONTENT=\"" + IMAGE + "\" />");
			SB.append("<META NAME=\"twitter:card\" CONTENT=\"summary_large_image\" />");
		}

		//タイプ
		switch (TYPE) {
			case WebSite: {
				SB.append("<META PROPERTY=\"og:type\" CONTENT=\"website\" />");
			}

			case Article: {
				SB.append("<META PROPERTY=\"og:type\" CONTENT=\"article\" />");
			}
		}

		return SB.toString();
	}
}
