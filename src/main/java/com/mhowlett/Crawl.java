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

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;

public class Crawl {

  public static HashMap<String, Person> crawlPeople() throws IOException, ClassNotFoundException {
    String url = "http://www.nownownow.com";
    String agent = "https://www.matthowlett.com";
    Document doc = Jsoup.connect(url)
        .userAgent(agent)
        .get();

    HashMap<String, Person> result = new HashMap<>();
    Elements lis = doc.select("li");
    lis.forEach(li -> {
      Elements names = li.select(".name");
      if (!names.isEmpty()) {
        Person p = new Person();
        Elements nowPageA = li.select(".name > a");
        if (!nowPageA.isEmpty()) {
          p.n3Url = nowPageA.first().attr("href").substring(2);
          p.name = nowPageA.first().text();
        } else {
          p.name = names.first().text();
        }
        Elements urls = li.select(".url > a");
        if (!urls.isEmpty()) {
          p.nowPageUrl = urls.first().attr("href");
        }
        Elements sts = li.select(".subtitle");
        if (!sts.isEmpty()) {
          p.subtitle = sts.first().text();
        }
        result.putIfAbsent(p.n3Url, p);
      }
    });

    return result;
  }
}
