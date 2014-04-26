package org.broadleafcommerce.core.web.processor;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.fragment.FragmentAndTarget;
import org.thymeleaf.fragment.WholeFragmentSpec;

public class HeadProcessorEx extends HeadProcessor {

	@Override
	protected FragmentAndTarget getFragmentAndTarget(Arguments arguments,
			Element element, boolean substituteInclusionNode) {
		String head = element.getAttributeValue("head");
		super.getFragmentAndTarget(arguments, element, substituteInclusionNode);
		return new FragmentAndTarget(
				head == null || head.length() == 0 ? HEAD_PARTIAL_PATH : head,
				WholeFragmentSpec.INSTANCE);
	}

}
