---
layout: post
title: Google Summer of Code is excluding half the world from participating
image: /images/holidays1.png
categories:
- python
---

I recently came across someone who wanted to mentor a Yosys VHDL frontent as a Google Summer of Code project. This sounded fun, so I wrote a proposal, noting that GSoC starts before my summer holiday, and planning accordingly. Long story short, there are limited spots and my proposal was not accepted. I have confirmed with the mentoring organization that my availability was the primary factor in this.

While I understand their decision, it seems odd from an organizational viewpoint. Surely others would have the same problem?
Indeed I heard from one person that they coped by just working ridiculous hours, while another said they never applied because of the mismatch. Google seems to be aware that this is an issue, stating in [their FAQ](https://developers.google.com/open-source/gsoc/faq#can_the_schedule_be_adjusted_if_my_school_ends_latestarts_early):

> **Can the schedule be adjusted if my school ends late/starts early?**
> No. We know that the schedule doesn't work for some students, but it's impossible to make a single timeline that works for everyone.
> Some organizations may allow a participant to start a little early or end a little late -- but this is usually measured in days, not weeks.
> The monthly evaluation dates cannot be changed.

But how big is this problem, and where do accepted proposals come from? I decided to find out. Wikipedia has a [long page of summer vacation dates](https://en.wikipedia.org/wiki/Summer_vacation) for each country, and there is also [this pdf](https://www.anefore.lu/wp-content/uploads/2017/09/11_School-Calendar-2017_18-Final-version-1.pdf) which contains the following helpful graphic.

![holidays](/images/holidays1.png)
![holidays](/images/holidays2.png)

**Most summer vacations are from July to August, while GSoC runs from May 27 to August 19**, excluding most of Europe and many other countries from participating. (unless you lie in your proposal or work 70 hours per week)

The next question is if this is reflected in accepted proposals. Since country of origin is not disclosed, this requires some digging. I scraped a few hundred names from the GSoC website, and scraped their location from a Linkedin search. This is of course not super reliable, but should give some indication.

```
      1 Argentina
      5 Australia
      1 Bangladesh
      6 Brazil
      1 Canada
      1 Chile
      7 China
      1 Denmark
      3 Egypt
      5 France
      9 Germany
      2 Ghana
      4 Greece
      2 Hong Kong
    212 India
      4 Indonesia
      1 Israel
      4 Italy
      2 Kazakhstan
      2 Kenya
      1 Lithuania
      2 Malaysia
      2 Mexico
      1 Nepal
      2 Nigeria
      1 Paraguay
      1 Peru
      3 Poland
      1 Portugal
      2 Qatar
      4 Romania
      4 Russian Federation
      1 Serbia
      4 Singapore
      1 South Africa
     10 Spain
      8 Sri Lanka
      2 Sweden
      2 Switzerland
      1 Tank
      2 Turkey
      4 Ukraine
      2 United Arab Emirates
      1 United Kingdom
     78 United States
     70 unknown
      1 Uruguay
      1 Uzbekistan
      3 Vietnam
```

**Holy moly, so many Indians**(212), followed by a large number of Americans(78), and then Spain(10), Germany(9), and the rest of the world. No Dutchies in this subset. For all European countries I counted a combined 51 participants, still a reasonable number. Even though Spain and Germany have the same holiday mismatch as the Netherlands. **Tell me your secret!** Interestingly, [Wikipedia states](https://en.wikipedia.org/wiki/Summer_vacation#India) that India has very short holidays, but special exceptions for summer programmes:

> Summer vacation lasts for no more than six weeks in most schools. The duration may decrease to as little as three weeks for older students, with the exception of two month breaks being scheduled to allow some high school and university students to participate in internship and summer school programmes.

Anyway, I think a big international company like Google could try to be a bit more flexible, and for example let students work for a subset of the monthly evaluation periods that align with their holiday.

### Appendix

To scrape the names, I scrolled down on [the project page](https://summerofcode.withgoogle.com/projects/) until I got bored, and then entered some JS in the browser console.

{% highlight javascript %}
Array.prototype.map.call(document.querySelectorAll(".project-card h2"), function(x) { return x.innerText })
{% endhighlight %}

I saved this to a file and wrote a Selenium script to search Linkedin. Likedin was being really annoying by serving me different versions of various pages with completely different html tags, so this only works half of the time.

{% highlight python %}
from selenium import webdriver
from selenium.common.exceptions import NoSuchElementException
import json
import time
from urllib.parse import urlencode

with open('data.json') as f:
    data = json.load(f)

driver = webdriver.Firefox()
driver.implicitly_wait(5)
driver.get('https://www.linkedin.com')

username = driver.find_element_by_id('login-email')
username.send_keys('email')
password = driver.find_element_by_id('login-password')
password.send_keys('password')
sign_in_button = driver.find_element_by_id('login-submit')
sign_in_button.click()

for name in data:
    try:
        first, last = name.split(' ', 1)
    except ValueError:
        continue
    if last.endswith('-1'):
        last = last[:-2]
    params = urlencode({"firstName": first, "lastName": last})
    driver.get("https://www.linkedin.com/search/results/people/?" + params)
    try:
        location = driver.find_element_by_css_selector('.search-result--person .subline-level-2').text
        print('"%s", "%s"' % (name, location))
    except NoSuchElementException:
        print('"%s", "%s"' % (name, 'unknown'))
        continue
{% endhighlight %}

And finally some quick Bash hax to count the countries. (All US locations only list their state)

{% highlight bash %}
cat output.csv | cut -d\" -f 4 | sed "s/Area$/Area, United States/i" | awk -F, '{print $NF}' | awk '{$1=$1};1' | sort | uniq -c
{% endhighlight %}
