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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;


public class WordMap {
  private final Map<String, Double> _weights = new HashMap<>();

  private static double posToWeight(String pos) {
    switch (pos) {
      case "NN":
        return 1.0;
      case "NNP":
        return 1.0;
      case "NNS":
        return 0.9;
      case "JJ":
        return 0.1;
      case "VB":
        return 0.1;
      case "VBP":
        return 0.1;
      default:
        return 0.0;
    }
  }

  public void add(String word, String pos) {
    double weight = posToWeight(pos);
    if (weight == 0.0) {
      return;
    }
    _weights.put(word, weight);
    // if (_weights.containsKey(word)) {
    //   _weights.put(word, _weights.get(word) + weight);
    // }
    // else {
    //   _weights.put(word, weight);
    // }
  }

  public Set<String> keySet() {
    return _weights.keySet();
  }

  public void forEach(BiConsumer<String, Double> f) {
    _weights.forEach(f);
  }

  public boolean containsKey(String k) {
    return _weights.containsKey(k);
  }

  public Double get(String k) {
    return _weights.get(k);
  }

  public String toString() {
    return new JSONObject(_weights).toString();
  }
}
