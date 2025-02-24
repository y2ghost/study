import csv


def process_csv(path):
    with open(path, newline='') as csvfile:
        reader = csv.reader(csvfile)
        next(reader, None)
        for row in reader:
            print(row)


if __name__ == '__main__':
    process_csv('books.csv')
