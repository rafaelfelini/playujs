package tags.playujs;

import groovy.lang.Closure;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import play.exceptions.TagInternalException;
import play.exceptions.TemplateExecutionException;
import play.exceptions.TemplateNotFoundException;
import play.templates.FastTags;
import play.templates.GroovyTemplate.ExecutableTemplate;
import play.templates.JavaExtensions;
import play.templates.Template;
import play.templates.TemplateLoader;

public class Javascript extends FastTags {

	public static void _escapejs(Map<?, ?> args, Closure body, PrintWriter out, ExecutableTemplate template, int fromLine) {
		if (!args.containsKey("arg") || args.get("arg") == null) {
			final String msg = "Specify a template name";
			TagInternalException internalException = new TagInternalException(msg);
			throw new TemplateExecutionException(template.template, fromLine, msg, internalException);
		}

		try {
			Template tmpl = TemplateLoader.load(args.get("arg").toString());
			Map<String, Object> newArgs = new HashMap<String, Object>();
			newArgs.putAll(template.getBinding().getVariables());
			newArgs.put("_isInclude", true);
			//dont write to the response.out, need to be escaped before.
			newArgs.remove("out");

			String content = tmpl.render(newArgs);
			out.print(JavaExtensions.escapeJavaScript(content));
		} catch (TemplateNotFoundException e) {
			throw new TemplateNotFoundException(e.getPath(), template.template, fromLine);
		}
	}
	
}