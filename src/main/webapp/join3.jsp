<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%String title = request.getParameter("title");%>
<%
	boolean bool[] = new boolean[10];
	for(int i=0; i<=10; i++) {
		
	}
%>
<%
	String fromDate = request.getParameter("fromDate");
	String toDate = request.getParameter("toDate");
	int fromDate1 = Integer.parseInt(fromDate);
	int toDate1 = Integer.parseInt(toDate);		
	int i;

%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>키워드로 장소검색하고 목록으로 표출하기 + 컨트롤 표시 + 선의 거리 검색 + 버튼 링크연결하기 + 목록 만들기</title>
</head>

<!-- join CSS link -->
<link href="resources/css/joinStyle.css" rel="stylesheet">

<!-- Favicons -->
<link href="resources/images/favicon3.png" rel="icon">

<!-- ======= Navbar ======= -->
<%@ include file="/WEB-INF/views/include/teamNavigation2.jsp" %>

<body>   


<!-- 장소 키워드/검색 -->
<div class="map_wrap">
    <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;"></div>
    <div id="menu_wrap" class="bg_white">
        <div class="option">
            <div>
                <form onsubmit="searchPlaces(); return false;">
                    키워드 : <input type="text" value="인천" id="keyword" size="15"> 
                    <button type="submit">검색하기</button> 
                </form>
            </div>
        </div>
        <hr>
        <ul id="placesList"></ul>
        <div id="pagination"></div>
    </div>


<!-- KAKAO api key 입력 --> 
<script type="text/javascript" src="//dapi.kakao.com/v2/maps/sdk.js?appkey=f971e17e00ae485ab7a78301e95a6fe8&libraries=services"></script>

<script>
	// 마커를 담을 배열입니다
	var markers = [];
	
	var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
	    mapOption = {
	        center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
	        level: 5 // 지도의 확대 레벨
	    };  
	
	// 지도를 생성합니다    
	var map = new kakao.maps.Map(mapContainer, mapOption); 
	
	// 장소 검색 객체를 생성합니다
	var ps = new kakao.maps.services.Places();  
	
	// 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
	var infowindow = new kakao.maps.InfoWindow({zIndex:1});
	
	// 키워드로 장소를 검색합니다
	searchPlaces();
	
	// 키워드 검색을 요청하는 함수입니다
	function searchPlaces() {
	
	    var keyword = document.getElementById('keyword').value;
	
	    if (!keyword.replace(/^\s+|\s+$/g, '')) {
	        alert('키워드를 입력해주세요!');
	        return false;
	    }
	
	    // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
	    ps.keywordSearch( keyword, placesSearchCB); 
	}
	
	// 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
	function placesSearchCB(data, status, pagination) {
	    if (status === kakao.maps.services.Status.OK) {
	
	        // 정상적으로 검색이 완료됐으면
	        // 검색 목록과 마커를 표출합니다
	        displayPlaces(data);
	
	        // 페이지 번호를 표출합니다
	        displayPagination(pagination);
	
	    } else if (status === kakao.maps.services.Status.ZERO_RESULT) {
	
	        alert('검색 결과가 존재하지 않습니다.');
	        return;
	
	    } else if (status === kakao.maps.services.Status.ERROR) {
	
	        alert('검색 결과 중 오류가 발생했습니다.');
	        return;
	
	    }
	}
	
	// 검색 결과 목록과 마커를 표출하는 함수입니다
	function displayPlaces(places) {
	
	    var listEl = document.getElementById('placesList'), 
	    menuEl = document.getElementById('menu_wrap'),
	    fragment = document.createDocumentFragment(), 
	    bounds = new kakao.maps.LatLngBounds(), 
	    listStr = '';
	    
	    // 검색 결과 목록에 추가된 항목들을 제거합니다
	    removeAllChildNods(listEl);
	
	    // 지도에 표시되고 있는 마커를 제거합니다
	    removeMarker();
	    
	    for ( var i=0; i<places.length; i++ ) {
	
	        // 마커를 생성하고 지도에 표시합니다
	        var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
	            marker = addMarker(placePosition, i), 
	            itemEl = getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다
	
	        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
	        // LatLngBounds 객체에 좌표를 추가합니다
	        bounds.extend(placePosition);
	
	        // 마커와 검색결과 항목에 click 했을때
	        // 해당 장소에 인포윈도우에 장소명을 표시합니다
	        // rightclick 했을 때는 인포윈도우를 닫습니다
	        (function(marker, title) {
	            kakao.maps.event.addListener(marker, 'click', function() {
	                displayInfowindow(marker, title);
	            });
	
	            kakao.maps.event.addListener(marker, 'rightclick', function() {
	                infowindow.close();
	            });
	
	            itemEl.onmouseover =  function () {
	                displayInfowindow(marker, title);
	            };
	
	            itemEl.onmouseout =  function () {
	                infowindow.close();
	            };
	        })(marker, places[i].place_name);
	
	        fragment.appendChild(itemEl);
	    }
	
	    // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
	    listEl.appendChild(fragment);
	    menuEl.scrollTop = 0;
	
	    // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
	    map.setBounds(bounds);
	}
	
	// 검색결과 항목을 Element로 반환하는 함수입니다
	function getListItem(index, places) {
	
	    var el = document.createElement('li'),
	    itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
	                '<div class="info">' +
	                '   <h5>' + places.place_name + '</h5>';
	
	    if (places.road_address_name) {
	        itemStr += '    <span>' + places.road_address_name + '</span>' +
	                    '   <span class="jibun gray">' +  places.address_name  + '</span>';
	    } else {
	        itemStr += '    <span>' +  places.address_name  + '</span>'; 
	    }
	                 
	      itemStr += '  <span class="tel">' + places.phone  + '</span>' +
	                '</div>';           
	
	    el.innerHTML = itemStr;
	    el.className = 'item';
	
	    return el;
	}
	
	// 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
	function addMarker(position, idx, title) {
	    var imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
	        imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
	        imgOptions =  {
	            spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
	            spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
	            offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
	        },
	        markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
	            marker = new kakao.maps.Marker({
	            position: position, // 마커의 위치
	            image: markerImage 
	        });
	
	    marker.setMap(map); // 지도 위에 마커를 표출합니다
	    markers.push(marker);  // 배열에 생성된 마커를 추가합니다
	
	    return marker;
	}
	
	// 지도 위에 표시되고 있는 마커를 모두 제거합니다
	function removeMarker() {
	    for ( var i = 0; i < markers.length; i++ ) {
	        markers[i].setMap(null);
	    }   
	    markers = [];
	}
	
	// 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
	function displayPagination(pagination) {
	    var paginationEl = document.getElementById('pagination'),
	        fragment = document.createDocumentFragment(),
	        i; 
	
	    // 기존에 추가된 페이지번호를 삭제합니다
	    while (paginationEl.hasChildNodes()) {
	        paginationEl.removeChild (paginationEl.lastChild);
	    }
	
	    for (i=1; i<=pagination.last; i++) {
	        var el = document.createElement('a');
	        el.href = "#";
	        el.innerHTML = i;
	
	        if (i===pagination.current) {
	            el.className = 'on';
	        } else {
	            el.onclick = (function(i) {
	                return function() {
	                    pagination.gotoPage(i);
	                }
	            })(i);
	        }
	
	        fragment.appendChild(el);
	    }
	    paginationEl.appendChild(fragment);
	}
	
	
	
	// 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
	// 인포윈도우에 장소명을 표시합니다
	
	function displayInfowindow(marker, title) {
	
	   var content = '<div style="padding:5px;z-index:1; width:200px;">' + title + '<br>' +
	    '<button id="btn1">일정 추가</button></div>';
	    infowindow.setContent(content);
	    infowindow.open(map, marker);
	    document.getElementById('btn1').onclick = function() {select2()};
	    document.getElementById('day1').onclick = function() {day1()};
	    document.getElementById('day2').onclick = function() {day2()};
	    document.getElementById('day3').onclick = function() {day3()};
	    document.getElementById('day4').onclick = function() {day4()};
	    document.getElementById('day5').onclick = function() {day5()};
	    document.getElementById('day6').onclick = function() {day6()};
	    document.getElementById('day7').onclick = function() {day7()};
	    document.getElementById('day8').onclick = function() {day8()};
	    document.getElementById('day9').onclick = function() {day9()};
	    document.getElementById('day10').onclick = function() {day10()};
	   
	    
	    function select2() {
	       
	       if(check1)
	       {
	         const table = document.getElementById('demo1');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title;
	       }
	         
	       else if(check2)
	       {
	         const table = document.getElementById('demo2');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title;       
	       }
	         
	       else if(check3)
	       {
	         const table = document.getElementById('demo3');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check4)
	       {
	         const table = document.getElementById('demo4');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check5)
	       {
	         const table = document.getElementById('demo5');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check6)
	       {
	         const table = document.getElementById('demo6');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check7)
	       {
	         const table = document.getElementById('demo7');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check8)
	       {
	         const table = document.getElementById('demo8');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check9)
	       {
	         const table = document.getElementById('demo9');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	       
	       else if(check10)
	       {
	         const table = document.getElementById('demo10');
	         const newRow = table.insertRow(-1);
	         
	         const newCell1 = newRow.insertCell(0);
	         newCell1.innerText = title; 
	       }
	
	    }
	
	   function day1() {
	      check1 = true;
	      check2 = false;
	      check3 = false;
	      check4 = false;
	      check5 = false;
	      check6 = false;
	      check7 = false;
	      check8 = false;
	      check9 = false;
	      check10 = false;
	      alert("day1에 일정을 추가합니다.");
	   }
	   
	   function day2() {
	      check1 = false;
	      check2 = true;
	      check3 = false;
	      check4 = false;
	      check5 = false;
	      check6 = false;
	      check7 = false;
	      check8 = false;
	      check9 = false;
	      check10 = false;
	      alert("day2에 일정을 추가합니다.");
	   }
	   function day3() {
	      check1 = false;
	      check2 = false;
	      check3 = true;
	      check4 = false;
	      check5 = false;
	      check6 = false;
	      check7 = false;
	      check8 = false;
	      check9 = false;
	      check10 = false;
	      alert("day3에 일정을 추가합니다.");
	   }
	
	   function day4() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = true;
	         check5 = false;
	         check6 = false;
	         check7 = false;
	         check8 = false;
	         check9 = false;
	         check10 = false;
	         alert("day4에 일정을 추가합니다.");
	      }
	   
	   function day5() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = false;
	         check5 = true;
	         check6 = false;
	         check7 = false;
	         check8 = false;
	         check9 = false;
	         check10 = false;
	         alert("day5에 일정을 추가합니다.");
	      }
	   
	   function day6() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = false;
	         check5 = false;
	         check6 = true;
	         check7 = false;
	         check8 = false;
	         check9 = false;
	         check10 = false;
	         alert("day6에 일정을 추가합니다.");
	      }
	   
	   function day7() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = false;
	         check5 = false;
	         check6 = false;
	         check7 = true;
	         check8 = false;
	         check9 = false;
	         check10 = false;
	         alert("day7에 일정을 추가합니다.");
	      }
	   
	   function day8() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = false;
	         check5 = false;
	         check6 = false;
	         check7 = false;
	         check8 = true;
	         check9 = false;
	         check10 = false;
	         alert("day8에 일정을 추가합니다.");
	      }
	   
	   function day9() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = false;
	         check5 = false;
	         check6 = false;
	         check7 = false;
	         check8 = false;
	         check9 = true;
	         check10 = false;
	         alert("day9에 일정을 추가합니다.");
	      }
	   
	   function day10() {
	         check1 = false;
	         check2 = false;
	         check3 = false;
	         check4 = false;
	         check5 = false;
	         check6 = false;
	         check7 = false;
	         check8 = false;
	         check9 = false;
	         check10 = true;
	         alert("day10에 일정을 추가합니다.");
	      }
	
	}
	
	 // 검색결과 목록의 자식 Element를 제거하는 함수입니다
	function removeAllChildNods(el) {   
	    while (el.hasChildNodes()) {
	        el.removeChild (el.lastChild);
	    }
	}
	 ///////////////////////
	
	   function deleteRow1() {
	        // table element 찾기
	       const table = document.getElementById('demo1');
	       const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	
	     }
	 
	   function deleteRow2() {
	        // table element 찾기
	        const table = document.getElementById('demo2');
	        
	        // 행(Row) 삭제
	       const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	      }
	   
	   function deleteRow3() {
	        // table element 찾기
	        const table = document.getElementById('demo3');
	        
	        // 행(Row) 삭제
	       const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	      }
	   
	   function deleteRow4() {
	       // table element 찾기
	       const table = document.getElementById('demo4');
	       
	       // 행(Row) 삭제
	      const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	     }
	   
	   function deleteRow5() {
	       // table element 찾기
	       const table = document.getElementById('demo5');
	       
	       // 행(Row) 삭제
	      const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	     }
	   
	   function deleteRow6() {
	       // table element 찾기
	       const table = document.getElementById('demo6');
	       
	       // 행(Row) 삭제
	      const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	     }
	   
	   function deleteRow7() {
	       // table element 찾기
	       const table = document.getElementById('demo7');
	       
	       // 행(Row) 삭제
	      const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	     }
	   
	   function deleteRow8() {
	       // table element 찾기
	       const table = document.getElementById('demo8');
	       
	       // 행(Row) 삭제
	     const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	     }
	   
	   function deleteRow9() {
	       // table element 찾기
	       const table = document.getElementById('demo9');
	       
	       // 행(Row) 삭제
	     const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");   
	       }
	     }
	   
	   function deleteRow10() {
	       // table element 찾기
	       const table = document.getElementById('demo10');
	
	       // 행(Row) 삭제
	     const event = document.getElementsByTagName('tr'); 
	        // 행(Row) 삭제
	       if(event.length != <%=(toDate1-fromDate1)+1%>)
	       {
	          const newRow = table.deleteRow(-1);
	       }
	       else
	       {
	          alert("삭제할 일정이 없습니다.");  f
	       }
	     }
</script>

<!-- 검색창 왼쪽 -->
    <div id="menu_wrap1" class="bg_white">
        <div id="hi" class="option">일정</div>
        <hr>
<%

   for(i=1;i<=(toDate1-fromDate1)+1;i++)
   {
%>
   <table id="demo<%=i%>">
      <tr>
         <th><button id="day<%=i%>" class="row<%=i%>">Day<%=i %></button></th>
         <th><input type='button' class="row<%=i%>" value='일정 삭제' onclick='deleteRow<%=i %>(0)' /></th>
      </tr>
   </table>
<%
   }      
%>
          <button id="dayPlus">일 추가</button>
    </div>
</div>


</body>
</html>