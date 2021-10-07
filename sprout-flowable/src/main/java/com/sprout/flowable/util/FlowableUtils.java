package com.sprout.flowable.util;

import org.apache.commons.io.FileUtils;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.repository.ProcessDefinition;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public final class FlowableUtils {

    /**
     * 导出图片文件到硬盘
     *
     * @return 文件的全路径
     */
    public static String exportDiagramToFile(RepositoryService repositoryService, ProcessDefinition processDefinition, String exportDir) throws IOException {
        String diagramResourceName = processDefinition.getDiagramResourceName();
        String key = processDefinition.getKey();
        int version = processDefinition.getVersion();
        String diagramPath = "";
        InputStream resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), diagramResourceName);
        byte[] b = new byte[resourceAsStream.available()];

        resourceAsStream.read(b, 0, b.length);

        // create file if not exist
        String diagramDir = exportDir + "/" + key + "/" + version;
        File diagramDirFile = new File(diagramDir);
        if (!diagramDirFile.exists()) {
            diagramDirFile.mkdirs();
        }
        diagramPath = diagramDir + "/" + diagramResourceName;
        File file = new File(diagramPath);

        // 文件存在退出
        if (file.exists()) {
            // 文件大小相同时直接返回否则重新创建文件(可能损坏)
            return diagramPath;
        } else {
            file.createNewFile();
        }
        // wirte bytes to file
        FileUtils.writeByteArrayToFile(file, b, true);
        return diagramPath;
    }
}
