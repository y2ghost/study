sample_dict = {'first_name': 'y1', 'last_name': 'y2', 'email': 'yy@126.com'}
print(sample_dict)
numbers = dict(one=1, two=2, three=3)
print(numbers)
info_list = [('first_name', 'y1'), ('last_name', 'y2'),
             ('email', 'yy@126.com')]
info_dict = dict(info_list)
print(info_dict)
print(sample_dict['first_name'])
print('address' in sample_dict)
print('address' not in sample_dict)
print('email' in sample_dict)
print(sample_dict.get('address'))
print(sample_dict.get('address', 'NotFound'))
copied_dict = sample_dict.copy()
sample_dict.clear()
print(sample_dict)
print(copied_dict)
print(copied_dict.items())
print(copied_dict.keys())
print(copied_dict.values())

sample_dict = {'first_name': 'y1', 'last_name': 'y2', 'email': 'yy@126.com'}
sample_dict.pop('something', 'Notfound!')
sample_dict.pop('first_name')
print(sample_dict)
sample_dict.popitem()
print(sample_dict)

sample_dict = {'first_name': 'y1', 'last_name': 'y2', 'email': 'yy@126.com'}
sample_dict['address'] = 'here here!'
print(sample_dict)
del sample_dict['email']
print(sample_dict)
