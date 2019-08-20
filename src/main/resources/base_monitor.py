# -*- coding: utf-8 -*-
import urllib2
import urllib
import os
import socket
import json
import psutil
import time
import ConfigParser
import sys
from itertools import dropwhile


class LocalService(object):
    def __init__(self):
        pass

    def get_timestamp(self):
        timestamp = 0
        try:
            timestamp = int(time.time())
        except Exception, e:
            pass

        return timestamp


    def get_sysinfo(self):
        try:
            ip = ""
            try:
                s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
                s.connect(('8.8.8.8', 80))
                ip = s.getsockname()[0]
            finally:
                s.close()

            sysinfo = {
                # 'hostname': socket.gethostname(),
                # 'os': platform.platform(),
                'load_avg': os.getloadavg(),
                # 'num_cpus': psutil.cpu_count(),
                # 'ip': socket.gethostbyname(socket.gethostname())
                'ip': ip
            }
        except Exception, e:
            print(e)
            return None
        return sysinfo

    def get_memory(self):
        try:
            return psutil.virtual_memory()._asdict()
        except Exception, e:
            print(e)
            return None

    def get_swap_memory(self):
        try:
            return psutil.swap_memory()._asdict()
        except Exception, e:
            print(e)
            return None

    def get_swap_space(self):
        try:
            sm = psutil.swap_memory()
            swap = {
                'total': sm.total,
                'free': sm.free,
                'used': sm.used,
                'percent': sm.percent,
                'swapped_in': sm.sin,
                'swapped_out': sm.sout
            }
        except Exception, e:
            print(e)
            return None
        return swap

    def get_cpu(self):
        try:
            return psutil.cpu_times_percent(1)._asdict()
        except Exception, e:
            print(e)
            return None

    def get_cpu_cores(self):
        # get every core info
        try:
            return [c._asdict() for c in psutil.cpu_times_percent(0, percpu=True)]
        except Exception, e:
            print(e)
            return None

    def get_disks(self, all_partitions=False):
        disks = []
        try:
            for dp in psutil.disk_partitions(all_partitions):
                usage = psutil.disk_usage(dp.mountpoint)
                disk = {
                    'device': dp.device,
                    'mountpoint': dp.mountpoint,
                    'type': dp.fstype,
                    'options': dp.opts,
                    'space_total': usage.total,
                    'space_used': usage.used,
                    'space_used_percent': usage.percent,
                    'space_free': usage.free
                }
                disks.append(disk)
        except Exception, e:
            print(e)
            return None

        return disks

    def get_disks_counters(self, perdisk=True):
        """"
         - read_count:  number of reads
         - write_count: number of writes
         - read_bytes:  number of bytes read
         - write_bytes: number of bytes written
         - read_time:   time spent reading from disk (in milliseconds)
         - write_time:  time spent writing to disk (in milliseconds)
        """
        try:
            return dict((dev, c._asdict()) for dev, c in psutil.disk_io_counters(perdisk=perdisk).iteritems())
        except Exception, e:
            print(e)
            return None

    def get_users(self):
        try:
            return [u._asdict() for u in psutil.users()]
        except Exception, e:
            print(e)
            return None

    def get_network_iostat(self):
        try:
            data = psutil.net_io_counters()
            if data and len(data) == 8:
                netifs = {
                    'bytes_sent': data[0],
                    'bytes_recv': data[1]
                }
            else:
                return None
        except Exception, e:
            print(e)
            return None

        return netifs

    def get_tcp(self):
        sys_st = {
            "00": "ERROR_STATUS",
            "01": "TCP_ESTABLISHED",
            "02": "TCP_SYN_SENT",
            "03": "TCP_SYN_RECV",
            "04": "TCP_FIN_WAIT1",
            "05": "TCP_FIN_WAIT2",
            "06": "TCP_TIME_WAIT",
            "07": "TCP_CLOSE",
            "08": "TCP_CLOSE_WAIT",
            "09": "TCP_LAST_ACK",
            "0A": "TCP_LISTEN",
            "0B": "TCP_CLOSING",
        }

        tcp_static_dict = {}
        try:
            with open("/proc/net/tcp") as f:
                for line in dropwhile(lambda line: line.strip().startswith('sl'), f):
                    tcp_status_code = line.split()[3]
                    if sys_st.has_key(tcp_status_code):
                        if tcp_static_dict.get(sys_st[tcp_status_code], None) is None:
                            tcp_static_dict[sys_st[tcp_status_code]] = 1
                        else:
                            tcp_static_dict[sys_st[tcp_status_code]] += 1
        except Exception, e:
            pass

        return tcp_static_dict

    def get_tcp_timeout(self):
        result = {}
        try:
            with open("/proc/net/netstat") as f:
                name_str = f.readline()
                value_str = f.readline()

                name_list = name_str.split(" ")
                value_list = value_str.split(" ")
                if len(name_list) == len(value_list):
                    num = len(name_list)
                    for i in range(0, num):
                        if name_list[i] == "TCPTimeouts":
                            result["TCPTimeouts"] = value_list[i]
                        if name_list[i] == "TCPBacklogDrop":
                            result["TCPBacklogDrop"] = value_list[i]

        except Exception, e:
            pass

        return result

    def get_dove_cpu_load(self, data):
        result = []
        try:
            if "load_avg" in data:
                cpu_load01 = {
                    "name": "cpu_load01",
                    "value": data['load_avg'][0]
                }
                result.append(cpu_load01)
            else:
                raise AssertionError("load_avg is none")

        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_cpu_data(self):
        result = []
        try:
            cpu_data = self.get_cpu()
            for name in cpu_data:
                if name == "idle":
                    tmp = {
                        "name": "cpu_idle_percent",
                        "value": cpu_data[name]
                    }
                    result.append(tmp)

                if name == "system":
                    tmp = {
                        "name": "cpu_system_percent",
                        "value": cpu_data[name]
                    }
                    result.append(tmp)

                if name == "iowait":
                    tmp = {
                        "name": "cpu_iowait_percent",
                        "value": cpu_data[name]
                    }
                    result.append(tmp)

                if name == "user":
                    tmp = {
                        "name": "cpu_user_percent",
                        "value": cpu_data[name]
                    }
                    result.append(tmp)

            # if len(result) != 4:
            #     return []
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_mem_data(self):
        result = []
        try:
            mem_data = self.get_memory()
            for name in mem_data:
                if name == "total":
                    tmp = {
                        "name": "mem_MemTotal_bytes",
                        "value": mem_data[name]
                    }
                    result.append(tmp)

                if name == "free":
                    tmp = {
                        "name": "mem_MemFree_bytes",
                        "value": mem_data[name]
                    }
                    result.append(tmp)

            if len(result) != 2:
                return []
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_swap_mem_data(self):
        result = []
        try:
            mem_data = self.get_swap_memory()
            for name in mem_data:
                if name == "total":
                    tmp = {
                        "name": "mem_SwapTotal_bytes",
                        "value": mem_data[name]
                    }
                    result.append(tmp)

                if name == "free":
                    tmp = {
                        "name": "mem_SwapFree_bytes",
                        "value": mem_data[name]
                    }
                    result.append(tmp)

            if len(result) != 2:
                return []
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_disk_data(self, dataPath, homePath):
        result = []
        try:
            disk_usages = [(name, psutil.disk_usage(path)) for (
                name, path) in zip(('data', 'usr'), (dataPath, homePath))]
            result = [{
                "name": "disk_" + item + "_" + name,
                "value":  getattr(disk_usage, item, None)
            }
                for (name, disk_usage) in disk_usages
                for item in ('total', 'used')  # bytes
            ]

            if len(result) != 2 and len(result) != 4:
                return []
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result
    
    def get_dove_disk_io_counters_data(self , dataPath, homePath):
        result = []
        try:
            disk_io = psutil.disk_io_counters()
            result = [{
                "name" : "disk_" + item ,
                "value" : getattr(disk_io, item, 0)
            }
                for item in ('read_count', 'write_count','read_bytes','write_bytes','busy_time')
            ]
            if len(result) != 5 :
                return []
        except AssertionError as e:
            pass
        except Exception as e:
            pass 

        return result;

    def get_dove_net_traffic_data2(self):
        result = []
        try:
            data = psutil.net_io_counters()
            # (bytes_sent=14508483, bytes_recv=62749361, 
            # packets_sent=84311, packets_recv=94888, 
            # errin=0, errout=0, dropin=0, dropout=0)
            result.append({
                "name": "network_traffic_out",
                "value": data.bytes_sent
            })
            result.append({
                "name": "network_traffic_in",
                "value": data.bytes_recv
            })
            result.append({
                "name": "network_packets_sent",
                "value": data.packets_sent
            })
            result.append({
                "name": "network_packets_recv",
                "value": data.packets_recv
            })
        except Exception, e:
            print e

        return result

    def get_dove_net_traffic_data(self):
        # 获取一秒钟的网络进出流量
        result = []
        try:
            net_traffic_old = self.get_network_iostat()
            time.sleep(1)
            net_traffic_new = self.get_network_iostat()

            bytes_sent_old = 0
            bytes_recv_old = 0
            if "bytes_sent" in net_traffic_old:
                bytes_sent_old = net_traffic_old["bytes_sent"]
            if "bytes_recv" in net_traffic_old:
                bytes_recv_old = net_traffic_old["bytes_recv"]

            if bytes_recv_old == 0 or bytes_sent_old == 0:
                return []

            bytes_sent_new = 0
            bytes_recv_new = 0
            if "bytes_sent" in net_traffic_new:
                bytes_sent_new = net_traffic_new["bytes_sent"]
            if "bytes_recv" in net_traffic_new:
                bytes_recv_new = net_traffic_new["bytes_recv"]

            if bytes_recv_new == 0 or bytes_sent_new == 0:
                return []

            if bytes_sent_old <= bytes_sent_new:
                tmp = {
                    "name": "network_traffic_out",
                    "value": bytes_sent_new - bytes_sent_old
                }
                result.append(tmp)
            else:
                return []

            if bytes_recv_old <= bytes_recv_new:
                tmp = {
                    "name": "network_traffic_in",
                    "value": bytes_recv_new - bytes_recv_old
                }
                result.append(tmp)
            else:
                return []

            if len(result) != 2:
                return []

        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_tcp_data(self):
        result = []
        try:
            tcp_data = self.get_tcp()
            # tcp_data = psutil.
            if "TCP_CLOSE_WAIT" in tcp_data:
                tcp_close_wait = tcp_data["TCP_CLOSE_WAIT"]
            else:
                tcp_close_wait = 0
            tmp = {
                "name": "tcp_close_wait",
                "value": tcp_close_wait
            }
            result.append(tmp)

            if "TCP_CLOSING" in tcp_data:
                tcp_closing = tcp_data["TCP_CLOSING"]
            else:
                tcp_closing = 0
            tmp = {
                "name": "tcp_closing",
                "value": tcp_closing
            }
            result.append(tmp)

            if "TCP_ESTABLISHED" in tcp_data:
                tcp_established = tcp_data["TCP_ESTABLISHED"]
            else:
                tcp_established = 0
            tmp = {
                "name": "tcp_established",
                "value": tcp_established
            }
            result.append(tmp)

            return result
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_tcp_data2(self):
        result = []
        try:
            status_list = ["LISTEN", "ESTABLISHED", "TIME_WAIT", "CLOSE_WAIT", "LAST_ACK", "SYN_SENT"]
            status_temp = []

            net_connections = psutil.net_connections()
            for key in net_connections:
                status_temp.append(key.status)
        
            for status in status_list:
                result.append({
                    "name": "TCP_" + status,
                    "value": status_temp.count(status)
                })

            return result
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result


    def get_dove_tcp_timeout(self):
        result = []
        try:
            tcp_data = self.get_tcp_timeout()
            if "TCPTimeouts" in tcp_data:
                tcp_timeouts = tcp_data["TCPTimeouts"]
                tmp = {
                    "name": "tcp_Timeouts",
                    "value": long(tcp_timeouts)
                }
                result.append(tmp)
            else:
                raise AssertionError("get tcp timeouts error")

            if "TCPBacklogDrop" in tcp_data:
                tcp_backlogdrop = tcp_data["TCPBacklogDrop"]
                tmp = {
                    "name": "tcp_BacklogDrop",
                    "value": long(tcp_backlogdrop)
                }
                result.append(tmp)
            else:
                raise AssertionError("get tcp backlog drop error")

            return result
        except AssertionError as e:
            pass
        except Exception as e:
            pass

        return result

    def get_dove_monitor_data(self, dataPath, homePath):
        print dataPath
        print homePath
        # 如果某一个采集项出现异常,则所有数据不进行返回
        try:
            metric_list = []
            ip = ""

            monitor_data = []
            sysinfo = self.get_sysinfo()
            if "ip" in sysinfo:
                ip = sysinfo['ip']
            else:
                raise AssertionError("ip is empty")

            timestamp = self.get_timestamp()
            if timestamp == 0:
                raise AssertionError("get timestamp error")

            # get cpu load
            cpu_load_result = self.get_dove_cpu_load(sysinfo)
            if len(cpu_load_result) == 0:
                raise AssertionError("get cpu load error")
            monitor_data += cpu_load_result

            # get cpu data
            cpu_result = self.get_dove_cpu_data()
            if len(cpu_result) == 0:
                raise AssertionError("get cpu error")
            monitor_data += cpu_result

            # get memory data
            memory_result = self.get_dove_mem_data()
            if len(memory_result) == 0:
                raise AssertionError("get memory error")
            monitor_data += memory_result

            # get swap memory data
            swap_memory_result = self.get_dove_swap_mem_data()
            if len(swap_memory_result) == 0:
                raise AssertionError("get swap memory error")
            monitor_data += swap_memory_result

            # get disk data
            disk_result = self.get_dove_disk_data(dataPath, homePath)
            if len(disk_result) == 0:
                raise AssertionError("get disk error")
            monitor_data += disk_result

            # get disk io
            disk_result = self.get_dove_disk_io_counters_data(dataPath, homePath)
            if len(disk_result) == 0:
                raise AssertionError("get disk io error")
            monitor_data += disk_result

            # get network traffic data
            net_result = self.get_dove_net_traffic_data2()
            if len(net_result) == 0:
                raise AssertionError("get network traffic error")
            monitor_data += net_result

            # get tcp data
            tcp_result = self.get_dove_tcp_data2()
            if len(tcp_result) == 0:
                raise AssertionError("get tcp error")
            monitor_data += tcp_result

            my_temp = "{\"timestamp\":" + str(timestamp) + ","
            my_temp += "\"brokerIp\":\"" + ip + "\","
            for metric in monitor_data:
                my_temp += "\"" + metric['name'] + "\":" + str(metric['value']) + ","
            my_temp = my_temp[:-1];
            my_temp += "}"

            print(my_temp)
        except AssertionError as e:
            print e
        except Exception as e:
            print e

def main():
    print("test")
if __name__ == '__main__':
    server = LocalService()
    server.get_dove_monitor_data(sys.argv[1], sys.argv[2])
