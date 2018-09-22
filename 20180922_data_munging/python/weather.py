def parse_line(line):
    clean_line = line.lstrip()
    if clean_line.startswith("Dy") or clean_line.startswith("mo"):
        return []
    return clean_line.split()[0:3]


def convert_to_ints(text_array):
    cleaned  = map(lambda x: x.replace('*', ''), text_array)
    return list(map(int, cleaned))


def compute_temperature_spread(day_record):
    return [day_record[0], day_record[1]-day_record[2]]


def pick_smallest_temp_spread(temperatature_spread):
    return sorted(temperatature_spread, key=lambda x: x[1])[0]



def find_day_with_smallest_spread(filename):
    with open(filename) as f:
        records =[]
        for line in f:
            day_max_min = parse_line(line)
            if day_max_min != []:
                day_spread = compute_temperature_spread(convert_to_ints(day_max_min))
                records.append(day_spread)
    return pick_smallest_temp_spread(records)[0]


