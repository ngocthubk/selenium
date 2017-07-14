package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build;

public class DownloadEvent {
	private final Downloader downloader;

	public DownloadEvent(Downloader fileDownloader) {
		this.downloader = fileDownloader;
	}

	public Downloader getDownloader() {
		return downloader;
	}
}
