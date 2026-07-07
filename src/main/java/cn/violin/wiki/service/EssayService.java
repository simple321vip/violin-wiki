package cn.violin.wiki.service;

import cn.violin.wiki.dto.params.EssayRequest;
import cn.violin.wiki.dto.result.EssayResult;

import java.util.List;

/**
 * Article business interface.
 */
public interface EssayService {

    EssayResult get(String id);

    List<EssayResult> listByCategory(String categoryId);

    EssayResult autoSave(EssayRequest request);

    void delete(String id);

    void sortEssays(String categoryId, String[] ids);
}