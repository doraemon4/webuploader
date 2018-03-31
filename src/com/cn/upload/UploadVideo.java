package com.cn.upload;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

/**
 * 文件上传
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class UploadVideo extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		super.doGet(req, resp);
		doPost(req, resp);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);
		sfu.setHeaderEncoding("utf-8");

		String savePath = this.getServletConfig().getServletContext()
				.getRealPath("");
		String folad = "uploads";
		savePath = savePath + "\\" + folad + "\\";

		String fileMd5 = null;
		String chunk = null;

		try {
			List<FileItem> items = sfu.parseRequest(request);

			for (FileItem item : items) {
				if (item.isFormField()) {
					String fieldName = item.getFieldName();
					if (fieldName.equals("fileMd5")) {
						fileMd5 = item.getString("utf-8");
					}
					if (fieldName.equals("chunk")) {
						chunk = item.getString("utf-8");
					}
				} else {
					File file = new File(savePath + "/" + fileMd5);
					if (!file.exists()) {
						file.mkdir();
					}
					File chunkFile = new File(savePath + "/" + fileMd5 + "/"
							+ chunk);
					FileUtils.copyInputStreamToFile(item.getInputStream(),
							chunkFile);

				}
			}

		} catch (FileUploadException e) {
			e.printStackTrace();
		}

	}
}
