"""
Tests for allurebox.

See README.md about how to run this test module.
"""

import uuid
import urlparse

import requests  # that's external dependency, meh


# minimalistic report
REPORT = '''
<ns0:test-suite xmlns:ns0="urn:model.allure.qatools.yandex.ru" start="1458825124499" stop="1458825124500">
  <name>test_smoke</name>
  <labels/>
  <test-cases>
    <test-case start="1458825124499" status="passed" stop="1458825124500">
      <name>test_x</name>
      <labels/>
      <steps/>
      <attachments/>
    </test-case>
  </test-cases>
</ns0:test-suite>
'''

def test_smoke(pytestconfig):
    """
    Check that box responds well and that a report is openable
    """
    box_url = pytestconfig.getoption('boxurl')

    response = requests.post(urlparse.urljoin(box_url, 'generate'), 
                             files={'{}-testsuite.xml'.format(uuid.uuid4()): REPORT})

    assert response.status_code == 200

    assert 'url' in response.json()

    assert requests.get(response.json()['url']).status_code == 200
