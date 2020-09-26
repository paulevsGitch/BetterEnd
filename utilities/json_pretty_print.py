import json
import os

data = {}
def save_json(json_file):
	with open(json_file) as read_file:
		data = json.load(read_file)
	with open(json_file, "w") as data_file:
		json.dump(data, data_file, indent=4, sort_keys=True)

path = "D:\\BetterEnd\\BetterEnd_1.16.3\\utility_res\\item"

files = []
# r=root, d=directories, f = files
for r, d, f in os.walk(path):
    for file in f:
        if '.json' in file:
            files.append(os.path.join(r, file))

for f in files:
	print(f)
	save_json(f)