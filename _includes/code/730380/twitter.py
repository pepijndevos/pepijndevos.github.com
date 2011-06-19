import json
import webbrowser
import urllib2
import urllib

#http://dev.twitter.com/pages/streaming_api_methods#track
kwds = "#durftevragen photoshop"
# Twitter username
uname = ""
#Twitter password
pwd = ""

password_mgr = urllib2.HTTPPasswordMgrWithDefaultRealm()

top_level_url = "http://stream.twitter.com"
password_mgr.add_password(None, top_level_url, uname, pwd)

handler = urllib2.HTTPBasicAuthHandler(password_mgr)

opener = urllib2.build_opener(handler)

data = opener.open('http://stream.twitter.com/1/statuses/filter.json?track=' + urllib.quote_plus(kwds))

for line in data:
    j = json.loads(line)
    url = 'http://twitter.com/' + j['user']['screen_name'] + "/status/" + j['id_str']
    print url
    print('\a')
    webbrowser.open(url)
