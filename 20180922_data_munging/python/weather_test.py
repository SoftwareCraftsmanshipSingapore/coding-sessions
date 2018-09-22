import pytest
import weather
'''
  Dy MxT   MnT   AvT   HDDay  AvDP 1HrP TPcpn WxType PDir AvSp Dir MxS SkyC MxR MnR AvSLP

   1  88    59    74          53.8       0.00 F       280  9.6 270  17  1.6  93 23 1004.5
   2  79    63    71          46.5       0.00         330  8.7 340  23  3.3  70 28 1004.5
   3  77    55    66          39.6       0.00         350  5.0 350   9  2.8  59 24 1016.8
'''

def test_it_works():
    assert 1 == 1

def test_can_read_good_data():
   line = '  1  88    59    74          53.8       0.00 F       280  9.6 270  17  1.6  93 23 1004.5'
   assert ['1', '88', '59'] == weather.parse_line(line)

def test_can_handle_star():
   assert [1,88, 59] == weather.convert_to_ints(['1', '88', '59*'])

def test_compute_temperature_spread():
   assert [1,1] == weather.compute_temperature_spread([1, 10, 9])


def test_pick_smallest_temp_spread():
    rec = [ [1,1], [2,2] ]
    assert [1,1] == weather.pick_smallest_temp_spread(rec)


def test_can_convert_to_ints():
   assert [1, 2, 3] == weather.convert_to_ints(['1', '2', '3'])


def test_can_handle_header():
   line = '  Dy MxT   MnT   AvT   HDDay  AvDP 1HrP TPcpn WxType PDir AvSp Dir MxS SkyC MxR MnR AvSLP'
   assert [] == weather.parse_line(line)


def test_can_handle_blankline():
   line = ''
   assert [] == weather.parse_line(line)


def test_can_handle_last_line():
   line = 'mo  82.9  60.5  71.7    16  58.8       0.00              6.9          5.3'
   assert [] == weather.parse_line(line)


def test_end2end():
    assert 3 == weather.find_day_with_smallest_spread('weather_test_data.dat')
