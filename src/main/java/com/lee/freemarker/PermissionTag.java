package com.lee.freemarker;

import freemarker.core.Environment;
import freemarker.template.TemplateDirectiveBody;
import freemarker.template.TemplateException;
import freemarker.template.TemplateModelException;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.web.util.WebUtils;

import com.lee.common.Constants;

/**
 * <p>Equivalent to {@link org.apache.shiro.web.tags.PermissionTag}</p>
 */
public abstract class PermissionTag extends SecureTag {
    String getName(Map params) {
        return getParam(params, "name");
    }
    
    @Override
    protected void verifyParameters(Map params) throws TemplateModelException {
        String permission = getName(params);

        if (permission == null || permission.length() == 0) {
            throw new TemplateModelException("The 'name' tag attribute must be set.");
        }
    }

    @Override
    public void render(Environment env, Map params, TemplateDirectiveBody body) throws IOException, TemplateException {
        String p = getName(params);

        boolean show = showTagBody(p);
        if (show) {
            renderBody(env, body);
        }
    }

    protected boolean isPermitted(String p) {
    	Set<String> premissions = (Set<String>) WebUtils.getSessionAttribute(getRequest(), Constants.premissions);
		return premissions==null?false:premissions.contains(p);
    }

    protected abstract boolean showTagBody(String p);
}
