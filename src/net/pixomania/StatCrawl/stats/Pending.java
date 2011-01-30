/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.pixomania.StatCrawl.stats;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 *
 * @author galaxyAbstractor
 */
@Entity
@Table(name = "pending", catalog = "StatCrawl", schema = "")
@NamedQueries({
    @NamedQuery(name = "Pending.findAll", query = "SELECT p FROM Pending p"),
    @NamedQuery(name = "Pending.findById", query = "SELECT p FROM Pending p WHERE p.id = :id"),
    @NamedQuery(name = "Pending.findByMd5", query = "SELECT p FROM Pending p WHERE p.md5 = :md5"),
    @NamedQuery(name = "Pending.findByUrl", query = "SELECT p FROM Pending p WHERE p.url = :url")})
public class Pending implements Serializable {
    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "md5")
    private String md5;
    @Basic(optional = false)
    @Column(name = "url")
    private String url;

    public Pending() {
    }

    public Pending(Integer id) {
        this.id = id;
    }

    public Pending(Integer id, String md5, String url) {
        this.id = id;
        this.md5 = md5;
        this.url = url;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        Integer oldId = this.id;
        this.id = id;
        changeSupport.firePropertyChange("id", oldId, id);
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        String oldMd5 = this.md5;
        this.md5 = md5;
        changeSupport.firePropertyChange("md5", oldMd5, md5);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        String oldUrl = this.url;
        this.url = url;
        changeSupport.firePropertyChange("url", oldUrl, url);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pending)) {
            return false;
        }
        Pending other = (Pending) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "net.pixomania.StatCrawl.stats.Pending[ id=" + id + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }

}
