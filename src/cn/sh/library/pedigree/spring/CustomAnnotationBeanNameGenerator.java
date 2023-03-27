package cn.sh.library.pedigree.spring;

import java.util.Map;
import java.util.Set;

import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.AnnotationBeanNameGenerator;
import org.springframework.core.type.AnnotationMetadata;

public class CustomAnnotationBeanNameGenerator extends
        AnnotationBeanNameGenerator {

	@Override
	protected String buildDefaultBeanName(BeanDefinition definition) {
		return super.buildDefaultBeanName(definition).replace("Impl", "");
	}

    @Override
    protected String determineBeanNameFromAnnotation(AnnotatedBeanDefinition annotatedDef) {
        AnnotationMetadata amd = annotatedDef.getMetadata();
        Set<String> types = amd.getAnnotationTypes();
        String beanName = null;
        for (String type : types) {
            Map<String, Object> attributes = amd.getAnnotationAttributes(type);
            if (isStereotypeWithNameValue(type, amd.getMetaAnnotationTypes(type), attributes)) {
                String value = (String) attributes.get("value");
                if (StringUtils.hasLength(value)) {
                    if (beanName != null && !value.equals(beanName)) {
                        throw new IllegalStateException("Stereotype annotations suggest inconsistent " +
                                "component names: '" + beanName + "' versus '" + value + "'");
                    }
                    beanName = value;
                }
            }
        }
        return beanName;
    }
}
