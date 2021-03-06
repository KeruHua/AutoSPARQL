package org.aksw.autosparql.tbsl.algorithm;

import static org.junit.Assert.*;
import java.util.*;
import org.aksw.autosparql.commons.knowledgebase.*;
import org.aksw.autosparql.tbsl.algorithm.learning.*;
import org.aksw.autosparql.tbsl.algorithm.learning.ranking.SimpleRankingComputation;
import org.aksw.autosparql.tbsl.algorithm.sparql.Slot;
import org.aksw.autosparql.tbsl.algorithm.sparql.SlotType;
import org.aksw.autosparql.tbsl.algorithm.util.Prominences;
import org.junit.*;
import com.hp.hpl.jena.query.ResultSet;

public class TBSLTest
{

	@Ignore
	public void testDBpediaLorenzBuehmann() throws Exception
	{
		//		String question = "Give me soccer clubs in Premier League.";
		String question = "Give me all Persons born in Berlin.";
		TemplateInstantiation ti = TbslDbpedia.INSTANCE.answerQuestion(question);
		ResultSet rs = DBpediaKnowledgebase.INSTANCE.querySelect(ti.getQuery());
		assertTrue(!rs.hasNext());
	}

	@Test
	public void testDBpediaDanBrown() throws Exception
	{
		//		String question = "Give me soccer clubs in Premier League.";
		String question = "Give me all books written by Dan Brown.";
		TemplateInstantiation ti = TbslDbpedia.INSTANCE.answerQuestion(question);
		ResultSet rs = DBpediaKnowledgebase.INSTANCE.querySelect(ti.getQuery());
		System.out.println(rs.nextSolution().toString());
		//		assertTrue(rs.nextSolution().toString().contains("http://diadem.cs.ox.ac.uk/ontologies/real-estate#"));
		//		System.out.println(ti.getQuery());
		//		System.out.println(rs.nextSolution());
	}

	@Before
	public void testProminence()
	{
		Entity bedrooms = new Entity("http://diadem.cs.ox.ac.uk/ontologies/real-estate#bedrooms", "number of bedrooms");
		Slot slot = new Slot("p1",SlotType.DATATYPEPROPERTY,Arrays.asList(new String[]{"BEDROOMS"}));
		HashMap<Slot, Collection<Entity>> map = new HashMap<>();
		map.put(slot, Collections.singleton(bedrooms));
		Map<Slot, Prominences> scores = new SimpleRankingComputation(OxfordKnowledgebase.INSTANCE).computeEntityProminenceScoresWithReasoner(map);
		assertTrue(scores.values().iterator().next().values().iterator().next()>800);
	}

	@Test
	public void testOxfordMoreThanTwoBedrooms() throws Exception
	{
		assertFalse(((LocalKnowledgebase)TbslOxford.INSTANCE.getKnowledgebase()).getModel().isEmpty());
		String question = "houses with more than 2 bedrooms.";
		TemplateInstantiation ti = TbslOxford.INSTANCE.answerQuestion(question);
		System.out.println(ti.getQuery());
		ResultSet rs = OxfordKnowledgebase.INSTANCE.querySelect(ti.getQuery());
		assertTrue(rs.nextSolution().toString().contains("http://diadem.cs.ox.ac.uk/ontologies/real-estate#"));
	}

}