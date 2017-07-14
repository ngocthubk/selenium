package com.uc4.ecc.plugins.actionbuilder.utils;

import java.io.IOException;

import com.uc4.ecc.plugins.actioncommon.utils.LoggingCategory;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.StreamResource;
import com.vaadin.server.StreamResource.StreamSource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinResponse;

public final class LazyFileDownloader extends FileDownloader {

	/**
	 * Provide both the {@link StreamSource} and the filename in an on-demand way.
	 */
	public interface LazyStreamResource extends StreamSource {

		String getFilename();

		void onFinish();
	}

	private final LazyStreamResource lazyResource;

	public LazyFileDownloader(LazyStreamResource resource) {
		super(new StreamResource(resource, ""));
		if (resource == null) {
			throw new IllegalArgumentException("resource may not be null");
		}
		this.lazyResource = resource;
	}

	@Override
	public boolean handleConnectorRequest(VaadinRequest request, VaadinResponse response, String path) throws IOException {
		try {
			this.getResource().setFilename(this.lazyResource.getFilename());
			return super.handleConnectorRequest(request, response, path);
		} finally {
			try {
				Thread.sleep(1000);
				this.lazyResource.onFinish();
			} catch (InterruptedException e) {
				LoggingCategory.getLogger().debug("Download sleep 1s before close diaglog", e);
			}
		}
	}

	private StreamResource getResource() {
		return (StreamResource) this.getResource("dl");
	}

}
