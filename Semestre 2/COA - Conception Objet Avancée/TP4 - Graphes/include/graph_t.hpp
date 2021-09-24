#ifndef __GRAPH_HPP__
#define __GRAPH_HPP__

#include <exception>
#include <map>
#include <vector>
#include <string>
#include <algorithm>

template<class ND, class ED> 
class Graph {
    struct Node{
        int node_id;
        ND data;
    };
    struct Edge{
        int edge_id;
        ED data;
        int source_id;
        int dest_id;
    };

    /* data structures */
    std::map<int, Node> nodes;
    std::map<int, Edge> edges;
    std::map<int, std::vector<int>> dests;
    int id_counter;
    int edge_counter;
;

public:

    Graph() {
    	id_counter      = 0;
    	edge_counter    = 0;
    }
    	
    Graph(const Graph &other) {
        id_counter      = other.id_counter;
        edge_counter    = other.edge_counter;
        nodes           = other.nodes;
        edges           = other.edges;
        dests           = other.dests;
    }


    inline int add_node(const ND &m) {
        Node node;
        node.node_id        = id_counter;
        node.data           = m;

        nodes[id_counter] = node;
        return(id_counter++);
    }

    inline bool node_exist(int id) const{
        for(std::pair<int,Node> p : nodes){
            if(p.first == id) return(true);
        }
        return false;
    }


    inline int add_edge(const ED &m, int source_id, int dest_id){
        Edge edge;
        edge.edge_id    = edge_counter;
        edge.data       = m;
        edge.source_id  = source_id;
        edge.dest_id    = dest_id;

        edges[edge_counter] = edge;
        return(edge_counter++);
    }
    
    inline void remove_node(int node_id) { /* todo */ }

    inline int search_node(const ND &m) const {
        for(std::pair<int,Node> p : nodes){
            if(p.second.data == m) return(p.second.node_id);
        }
    }

    inline ND get_node_data(int node_id) const {
        auto iterator = nodes.find(node_id);
        if(iterator == nodes.end()) throw NodeNotFound(node_id);
        return(iterator->second.data);

    }

    inline ED get_edge_data(int edge_id) const {
        auto iterator = edges.find(edge_id);
        if(iterator == edges.end()) throw EdgeNotFound(edge_id);
        return(iterator->second.data);
    }
t get_edge_source(int edge_id) const {
        auto iterator = edges.find(edge_id);
        if(iterator == edges.end()) throw EdgeNotFound(edge_id);
        return(iterator->second.source_id);
    }
    
    inline int get_edge_dest(int edge_id) const {
        auto iterator = edges.find(edge_id);
        if(iterator == edges.end()) throw EdgeNotFound(edge_id);
        return(iterator->second.dest_id);
    }

    std::vector<int> get_successors(int node_id) const {
        std::vector<int> ret;
        for(std::pair<int,Edge> p : edges){
            if(p.second.source_id == node_id) ret.push_back(p.first);
        }   
        return ret;
    }
    
    std::vector<int> get_predecessors(int node_id) const {
        std::vector<int> ret;
        for(std::pair<int,Edge> p : edges){
            if(p.second.dest_id == node_id) ret.push_back(p.first);
        }   
        return ret;
    }



   using Path=std::vector<int>;
    std::vector<Path> all_paths(int from, int to) const{
    /*    vector<Path> ret;
        for(int i : get_successors(from)){
            if(get_edge_dest(i) == to) ret.push_back(Path(i));
            else{
                for(Path v : all_paths_rec(Path(i), to)){
                    ret.push_back(v);
                }
            }
        }
        return(ret);*/
    }

    std::vector<Path> all_paths_rec(Path from, int to) const{
/*        vector<Path> ret;
        int last = from[from.size()];
        for(int i : get_successors(last)){
            if(get_edge_dest(i) == to){
                Path sol;
                for(int j : from){
                    sol.push_back(j);
                }
                sol.push_back(i);
            }
        }*/
    }


    Path shortest_path(int from, int to) const
        { /* todo */ }
};



template <typename ...Tp>
class EdgeData : public Tp ... {
    std::string str;
public:
    void set_string(const std::string &s) { str = s; }
    std::string get_string() const { return str; }
};


class RouteLenght {
    double l;
public:
    void set_lenght(double len) { l = len; }
    double get_lenght(double len) const { return l; }       
};



class AverageTime{
    int time;
public:
    void set_time(int t){
        time = t;
    }

    int get_time(){
        return(time);
    }
};


#endif