package org.broadleafcommerce.core.web.processor;

import java.math.BigDecimal;
import java.util.List;

import org.broadleafcommerce.core.catalog.RelatedProductDTOEx;
import org.broadleafcommerce.core.catalog.domain.PromotableProduct;
import org.broadleafcommerce.core.catalog.domain.RelatedProductDTO;
import org.broadleafcommerce.core.catalog.domain.RelatedProductTypeEnum;
import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

/**
 * 新增了 start 分页属性. 若catId, prodId都没指定，则啥也不做, 不设值！
 * 
 * @author Ju
 */
public class RelatedProductProcessorEx extends RelatedProductProcessor {

	@Override 
	protected void modifyModelAttributes(Arguments arguments, Element element) {
		RelatedProductDTO dto = buildDTO(arguments, element); 
		if (dto.getCategoryId() == null && dto.getProductId() == null)
			return; // 不往model里放值

		List<? extends PromotableProduct> relatedProducts = relatedProductsService
				.findRelatedProducts(dto);
		addToModel(arguments, getRelatedProductsResultVar(element),
				relatedProducts);
		addToModel(arguments, getProductsResultVar(element),
				convertRelatedProductsToProducts(relatedProducts));
	}

	private RelatedProductDTO buildDTO(Arguments args, Element element) {
		RelatedProductDTOEx relatedProductDTO = new RelatedProductDTOEx();
		String productIdStr = element.getAttributeValue("productId");
		String categoryIdStr = element.getAttributeValue("categoryId");
		String quantityStr = element.getAttributeValue("quantity");
		String startStr = element.getAttributeValue("start");
		String typeStr = element.getAttributeValue("type");

		if (productIdStr != null) {
			Object productId = StandardExpressionProcessor.processExpression(
					args, productIdStr);
			if (productId instanceof BigDecimal) {
				productId = new Long(((BigDecimal) productId).toPlainString());
			}
			relatedProductDTO.setProductId((Long) productId);
		}

		if (categoryIdStr != null) {
			Object categoryId = StandardExpressionProcessor.processExpression(
					args, categoryIdStr);
			if (categoryId instanceof BigDecimal) {
				categoryId = new Long(((BigDecimal) categoryId).toPlainString());
			}
			relatedProductDTO.setCategoryId((Long) categoryId);
		}

		if (quantityStr != null) {
			relatedProductDTO
					.setQuantity(((BigDecimal) StandardExpressionProcessor
							.processExpression(args, quantityStr)).intValue());
		}
		if (startStr != null) {
			BigDecimal s = (BigDecimal) StandardExpressionProcessor
					.processExpression(args, startStr);
			if (s != null)
				relatedProductDTO.setStart(s.intValue());
		}

		if (typeStr != null) {
			typeStr = (String) StandardExpressionProcessor
					.processExpression(args, typeStr);
			RelatedProductTypeEnum type = RelatedProductTypeEnum.getInstance(typeStr);
			if (type != null)
				relatedProductDTO.setType(type);
		}

		if ("false".equalsIgnoreCase(element
				.getAttributeValue("cumulativeResults"))) {
			relatedProductDTO.setCumulativeResults(false);
		}

		return relatedProductDTO;
	}

	private String getRelatedProductsResultVar(Element element) {
		String resultVar = element
				.getAttributeValue("relatedProductsResultVar");
		if (resultVar == null) {
			resultVar = "relatedProducts";
		}
		return resultVar;
	}

	private String getProductsResultVar(Element element) {
		String resultVar = element.getAttributeValue("productsResultVar");
		if (resultVar == null) {
			resultVar = "products"; 
		}
		return resultVar;
	}

}
