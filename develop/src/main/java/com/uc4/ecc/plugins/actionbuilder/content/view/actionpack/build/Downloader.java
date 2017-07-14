package com.uc4.ecc.plugins.actionbuilder.content.view.actionpack.build;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import com.uc4.ecc.plugins.actionbuilder.utils.LazyFileDownloader;
import com.uc4.ecc.plugins.actionbuilder.utils.LazyFileDownloader.LazyStreamResource;
import com.vaadin.server.FileDownloader;


public class Downloader implements LazyStreamResource {

	private static final long serialVersionUID = 1L;
	private final LazyFileDownloader downloader;
	private final String fileName;
	private final File file;

	public Downloader(File file) {
		this.downloader = new LazyFileDownloader(this);
		this.fileName = file.getName();
		this.file = file;
	}

	@Override
	public InputStream getStream() {
		try {
			return new FileInputStream(this.file);
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getFilename() {
		return this.fileName;
	}

	public FileDownloader getFileDownloader() {
		return this.downloader;
	}

	@Override
	public void onFinish() {}
	
	public File getFile() {
		return this.file;
	}

}
