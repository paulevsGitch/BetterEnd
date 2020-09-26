import json
import os
import re

data = {}
def save_json(json_file):
	with open(json_file) as read_file:
		data = json.load(read_file)
	dump = json.dumps(data, sort_keys=True, indent=4, separators=(',', ': '))
	new_data = re.sub('\n +', lambda match: '\n' + '\t' * (len(match.group().strip('\n')) / 3), dump)
	print >> open(json_file, 'w'), new_data

def scan_rec(path):
	for r, d, f in os.walk(path):
		for file in f:
			if '.json' in file:
				save_json(os.path.join(r, file))
				print("Saved " + file)
		for dir in d:
			scan_rec(os.path.join(r, dir))

scan_rec(os.path.abspath("./../src/main/resources/assets/betterend"))