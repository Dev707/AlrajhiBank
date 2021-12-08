import sys
#import boto3
from random import randint
number = sys.argv[1][1:len(sys.argv[1])]
code = randint(1000, 9999)
#client = boto3.client('sns', 'us-east-2')
#client.publish(PhoneNumber=f'+966{number}', Message=f'Authentication Code: {code}')
print(code)
