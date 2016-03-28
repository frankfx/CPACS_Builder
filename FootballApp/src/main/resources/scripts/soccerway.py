#import urllib3
#import urllib.request
import urllib2
import json
import re

## First, run following code in the console:
#chcp 65001
#set PYTHONIOENCODING=utf-8
## Then, start python do anything you want.

# C:\Users\frank\AppData\Local\Programs\Python\Python35-32\Scripts pip
# C:\Users\frank\AppData\Local\Programs\Python\Python35-32

data = {'all': [], 'home': [], 'away': []}
#teamId = "994"

def getTeamData(teamId, dataType):
	teamId = str(teamId)	

	dat = getData(teamId, dataType)
	
	l = []

	for val in list(dat) :
		#return list(val)
		l.append(list(val))	
	
	return l
		
def parseJson(jsonStr):
	"""
	Handles the parsing of the JSON object returned
	by Soccerway API (nr.soccerway.com/a/block_team_matches).
	
	Input: The JSON object returned by the API as a string
		
	Return: Two dimensional array of the match results

	"""
	jsonPy = json.loads(jsonStr)

	# Fetch the interesting part of inputted JSON obj
	content = jsonPy['commands'][0]['parameters']['content']

	# Remove uninteresting header and footer data
	cleanContent = content.split('</tbody>',1)[0].split('<thead',1)[1]

	# Split content by <tr> -tags (tr is shorthand for table row)
	p1 = re.compile(r'<tr[^<]+?>')
	splitted = p1.split(cleanContent)
	header = splitted[1] # First row is the table header data
	data = splitted[2:-1] # Rest are the match info 

	# Split content by <td> -tags (table columns) and clean other tags
	p2 = re.compile(r'<td[^<]+?>')
	f = lambda x: map(lambda y: re.sub('<[^<]+?>','',y).strip(), p2.split(x)[1:-2])
		
	return map(f, data)

def getData(teamId, matchType):
	""" 
	Return the cleaned match data in 2D array.
	Does simple caching of the GET queries, that is,
	same data is not queried twice.

	Input: Type of the matches, must be one of
	the following strings: 'all', 'away' or 'home'.

	Output: 2d array of match results

	"""
	if matchType not in ['all','away','home']:
		return []

	if not data[matchType]:
		url = "http://nr.soccerway.com/a/block_team_matches" \
                 "?block_id=page_team_1_block_team_matches_5" \
                 "&callback_params=%7B%22page%22%3A0%2C%22" \
                 "bookmaker_urls%22%3A%5B%5D%2C%22block_service_id" \
                 "%22%3A%22team_matches_block_teammatches%22%2C%22" \
                 "team_id%22%3A"+teamId+"%2C%22competition_id" \
                 "%22%3A0%2C%22filter%22%3A%22all%22%7D" \
                 "&action=filterMatches&params=%7B%22" \
                 "filter%22%3A%22"+matchType+"%22%7D"
			
		## if use urllib2 stuff
		http = urllib2.urlopen(url)			
		jsonStr = http.read()			
			
		## if use urllib3 stuff
		#http = urllib3.PoolManager()			
		#jsonStr = http.urlopen('GET', url).data.decode("utf-8")
			
		## else if use urllib.request stuff
		#http = urllib.request.urlopen(url)
		#jsonStr = http.read().decode("utf-8")
			
		data[matchType] = parseJson(jsonStr)
            
	return data[matchType]