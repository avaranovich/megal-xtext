package plugins;

import static com.google.common.collect.FluentIterable.from;
import static com.google.common.collect.Iterables.concat;

import org.softlang.megal.mi2.KB;
import org.softlang.megal.mi2.KBs;
import org.softlang.megal.mi2.Ref;
import org.softlang.megal.mi2.Relationship;
import org.softlang.megal.mi2.api.AbstractReasonerPlugin;
import org.softlang.megal.mi2.api.context.Context;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import plugins.util.Prelude;

public class TraceLinkReasoner extends AbstractReasonerPlugin {
	@Override
	public KB derive(Context context, Relationship relationship) {

		if (Prelude.isElementOfLanguage(relationship.getLeft(), "XML")
				&& Prelude.isElementOfLanguage(relationship.getRight(), "XSD")) {

			Multimap<String, String> ab = HashMultimap.create();
			// ...
			return KBs.builder().setEntities(from(concat(ab.keys(), ab.values())).toMap(x -> Ref.to("Fragment", false)))
					.setRelationships(null).setBindings(null).build();
		}
		return super.derive(context, relationship);
	}
}
