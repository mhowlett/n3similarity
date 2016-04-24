// This file is part of n3similarity
//
// n3similarity is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// n3similarity is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with n3similarity.  If not, see <http://www.gnu.org/licenses/>.

package com.mhowlett;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

  public static String[] tokenize(String s) {
    return s.split("[ \\.,\\(\\)\";]");
  }

  private static double similarity(Person p1, Person p2) {
    if (p1.words == null || p2.words == null) {
      return 0.0;
    }
    double sum = 0.0;
    Object[] p1words = p1.words.keySet().toArray();
    for (int i=0; i<p1words.length; ++i) {
      String word = (String)p1words[i];
      if (p2.words.containsKey(word)) {
        sum += p1.words.get(word) * p2.words.get(word);
      }
    }
    return sum;
  }

  private static void addSimilarityScores(HashMap<String, Person> ps) {
    ps.forEach((k1, v1) -> {
      if (v1.words != null) {
        v1.similar = new ArrayList<>();
        ps.forEach((k2, v2) -> {
          if (k2 != k1) {
            double sim = similarity(v1, v2);
            if (sim > 0.0) {
              StringNumber sn = new StringNumber();
              sn.s = v2.n3Url;
              sn.d = sim;
              v1.similar.add(sn);
            }
          }
        });
        v1.similar.sort((a, b) -> a.d < b.d ? 1 : (a.d > b.d ? -1 : 0));
      }
    });
  }

  private static void tagSubtitles(HashMap<String, Person> ps) {
    MaxentTagger tagger
        = new MaxentTagger("/data/taggers/stanford-postagger-2015-12-09/models/english-left3words-distsim.tagger");

    ps.forEach((k, p) -> {
      if (p.subtitle != null) {
        String tagged = tagger.tagString(p.subtitle);
        String[] words = tagged.split(" ");
        p.words = new WordMap();
        for (int i = 0; i < words.length; ++i) {
          String[] tokens = words[i].split("_");
          if (tokens.length == 2) {
            p.words.add(tokens[0], tokens[1]);
          }
        }
      }
    });
  }

  public static void displayResults(HashMap<String, Person> ps) {
    ps.forEach((k, v) -> {
      System.out.println("[" + v.n3Url + "] " + v.subtitle);
      if (v.similar != null) {
        for (int i=0; i<v.similar.size() && i < 4; ++i) {
          String id = v.similar.get(i).s;
          Person p = ps.get(id);
          System.out.println("   [" + p.n3Url + "] " + p.subtitle);
        }
        System.out.println("");
      }
    });
  }

  public static void main(String[] args) throws IOException, ClassNotFoundException {
    HashMap<String, Person> ps = Crawl.crawlPeople();
    tagSubtitles(ps);
    addSimilarityScores(ps);
    displayResults(ps);
  }

}
